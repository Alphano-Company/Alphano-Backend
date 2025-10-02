package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.dao.MatchRepository;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.match.dto.MatchJobMessage;
import com.alphano.alphano.domain.match.dto.response.MatchResponse;
import com.alphano.alphano.domain.match.exception.OpponentNotFoundException;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.submission.exception.SubmissionCodeKeyMissingException;
import com.alphano.alphano.domain.submission.exception.SubmissionNotFoundException;
import com.alphano.alphano.domain.userRating.dao.UserRatingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final JudgeJobPublisher judgeJobPublisher;
    private final ProblemRepository problemRepository;
    private final SubmissionQueryRepository submissionQueryRepository;
    private final MatchRepository matchRepository;
    private final UserRatingQueryRepository userRatingQueryRepository;

    @Transactional
    public MatchResponse create(Long problemId, Long userId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> ProblemNotFoundException.EXCEPTION);

        Submission mine = submissionQueryRepository.getDefaultSubmission(problemId, userId)
                .orElseThrow(() -> SubmissionNotFoundException.EXCEPTION);

        // 상대 선택
        Double rating = userRatingQueryRepository.findCurrentRating(problemId, userId);
        Submission opp = chooseOpponent(problemId, userId, rating);

        if (mine.getCodeKey() == null || opp.getCodeKey() == null)
            throw SubmissionCodeKeyMissingException.EXCEPTION;

        // 랜덤 시드 생성
        int seed = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        Match match = Match.createQueuedMatch(problem, mine, opp, seed);
        Match saved = matchRepository.save(match);

        // SNS 발행
        MatchJobMessage msg = new MatchJobMessage(
                problemId,
                saved.getId(),
                seed,
                new MatchJobMessage.Agent(mine.getUser().getId(), mine.getId(), mine.getLanguage(), mine.getCodeKey()),
                new MatchJobMessage.Agent(opp.getUser().getId(), opp.getId(), opp.getLanguage(), opp.getCodeKey())
        );
        String groupId = "problem-" + problemId;
        judgeJobPublisher.publishMatchRequest(msg, groupId);

        return MatchResponse.of(problemId, saved.getId(), opp.getUser().getId(), seed, MatchStatus.QUEUED);
    }

    private Submission chooseOpponent(Long problemId, Long userId, double rating) {
        double sigma = sigma(rating);
        double p = ThreadLocalRandom.current().nextGaussian();
        double x = rating + sigma * p;

        System.out.println("sigma = " + sigma);
        System.out.println("x = " + x);

        Double minDist = submissionQueryRepository.findMinDistance(problemId, userId, x);
        if (minDist == null) {
            throw OpponentNotFoundException.EXCEPTION;
        }

        List<Submission> opponents = submissionQueryRepository.findAllWithExactDistance(problemId, userId, x, minDist);
        if (opponents.isEmpty()) {
            throw OpponentNotFoundException.EXCEPTION;
        }

        int idx = ThreadLocalRandom.current().nextInt(opponents.size());
        return opponents.get(idx);
    }

    private double sigma(double rating) {
        if (0 <= rating && rating <= 500) {
            return -0.2 * rating + 200;
        } else if (500 < rating && rating <= 1500) {
            return -0.05 * rating + 125;
        } else if (1500 < rating && rating <= 2500) {
            return -0.04 * rating + 110;
        } else {
            return 10.0;
        }
    }
}