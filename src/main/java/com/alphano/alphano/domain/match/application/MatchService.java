package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.domain.match.dao.MatchRepository;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.match.dto.MatchJobMessage;
import com.alphano.alphano.domain.match.dto.response.MatchResponse;
import com.alphano.alphano.domain.match.dto.response.MatchResultResponse;
import com.alphano.alphano.domain.match.exception.MatchNotFoundException;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.exception.SubmissionCodeKeyMissingException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotFoundException;
import com.alphano.alphano.domain.userHistory.dao.UserHistoryRepository;
import com.alphano.alphano.domain.userHistory.domain.UserHistory;
import com.alphano.alphano.domain.userRating.dao.UserRatingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final JudgeJobPublisher judgeJobPublisher;
    private final ProblemRepository problemRepository;
    private final SubmissionQueryRepository submissionQueryRepository;
    private final MatchRepository matchRepository;
    private final UserRatingQueryRepository userRatingQueryRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final S3Service s3Service;
    private final OpponentMatcher opponentMatcher;

    @Transactional
    public MatchResponse create(Long problemId, Long userId) {
        Problem problem = findProblemById(problemId);
        Submission mine = findMyDefaultSubmission(problemId, userId);

        Double rating = userRatingQueryRepository.findCurrentRating(problemId, userId);
        Submission opp = opponentMatcher.chooseOpponent(problemId, userId, rating);

        validateCodeKeys(mine, opp);

        int seed = generateRandomSeed();
        Match match = Match.createQueuedMatch(problem, mine, opp, seed);
        matchRepository.save(match);

        publishMatchJob(problemId, match, mine, opp);

        return MatchResponse.of(problemId, match.getId(), opp.getUser().getId(), seed, MatchStatus.QUEUED);
    }

    public MatchResultResponse getMatchResult(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> MatchNotFoundException.EXCEPTION);

        if (match.getStatus() == MatchStatus.QUEUED
                || match.getStatus() == MatchStatus.RUNNING
                || match.getStatus() == MatchStatus.FAILED) {

            return MatchResultResponse.ofStatus(match);
        }

        List<UserHistory> histories = userHistoryRepository.findAllByMatchId(matchId);
        if (histories.size() != 2) {
            throw new IllegalStateException("매치에 대한 히스토리가 2개가 아닙니다. matchId: " + matchId);
        }

        UserHistory history1, history2;
        if (histories.get(0).getUser().getId().equals(match.getAgent1Id())) {
            history1 = histories.get(0);
            history2 = histories.get(1);
        } else {
            history1 = histories.get(1);
            history2 = histories.get(0);
        }

        return MatchResultResponse.from(match, history1, history2, s3Service);
    }

    // === `create` 메소드를 보조하는 private 헬퍼 메서드들 === //
    private Problem findProblemById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> ProblemNotFoundException.EXCEPTION);
    }

    private Submission findMyDefaultSubmission(Long problemId, Long userId) {
        return submissionQueryRepository.getDefaultSubmission(problemId, userId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);
    }

    private void validateCodeKeys(Submission mine, Submission opp) {
        if (mine.getCodeKey() == null || opp.getCodeKey() == null) {
            throw SubmissionCodeKeyMissingException.EXCEPTION;
        }
    }

    private int generateRandomSeed() {
        return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    private void publishMatchJob(Long problemId, Match match, Submission mine, Submission opp) {
        MatchJobMessage msg = new MatchJobMessage(
                problemId,
                match.getId(),
                match.getRandomSeed(),
                new MatchJobMessage.Agent(mine.getUser().getId(), mine.getId(), mine.getLanguage(), mine.getCodeKey()),
                new MatchJobMessage.Agent(opp.getUser().getId(), opp.getId(), opp.getLanguage(), opp.getCodeKey())
        );
        String groupId = "problem-" + problemId;
        judgeJobPublisher.publishMatchRequest(msg, groupId);
    }
}