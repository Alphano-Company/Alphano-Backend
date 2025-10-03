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

        List<Submission> candidates = submissionQueryRepository.findAllCandidatesDefaultSubmission(problemId, userId);

        int n = candidates.size();
        if (n == 0) {
            throw OpponentNotFoundException.EXCEPTION;
        }

        List<Long> candidateIds = candidates.stream()
                .map(submission -> submission.getUser().getId())
                .collect(Collectors.toList());

        Map<Long, Double> candidateRatings = userRatingQueryRepository.findCurrentRatings(problemId, candidateIds);

        int bestIdx = -1;
        double bestVal = Double.NEGATIVE_INFINITY;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < n; i++) {
            Submission submission = candidates.get(i);
            Long candidateId = submission.getUser().getId();
            double candidateRating = candidateRatings.get(candidateId);

            double d = Math.abs(rating - candidateRating);

            double li = -d / sigma;

            double epsilon = 1e-10;
            double u = epsilon + (1.0 - 2 * epsilon) * random.nextDouble();

            double gi = -Math.log(-Math.log(u));
            double val = li + gi;

            if (val > bestVal) {
                bestVal = val;
                bestIdx = i;
            }
        }

        if (bestIdx < 0) {
            throw OpponentNotFoundException.EXCEPTION;
        }

        return candidates.get(bestIdx);
    }

    private double sigma(double rating) {
        if (rating < 500) {
            return -0.2 * rating + 200;
        } else if (rating < 1500) {
            return -0.05 * rating + 125;
        } else if (rating < 2500) {
            return -0.04 * rating + 110;
        } else {
            return 10.0;
        }
    }
}