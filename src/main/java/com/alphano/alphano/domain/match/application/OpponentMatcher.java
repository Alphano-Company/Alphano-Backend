package com.alphano.alphano.domain.match.application;

import com.alphano.alphano.domain.match.exception.OpponentNotFoundException;
import com.alphano.alphano.domain.submission.dao.SubmissionQueryRepository;
import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.userRating.dao.UserRatingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpponentMatcher {

    private final SubmissionQueryRepository submissionQueryRepository;
    private final UserRatingQueryRepository userRatingQueryRepository;

    /**
     * 주어진 조건에 가장 적합한 상대방의 Submission을 선택
     * @param problemId 현재 문제 ID
     * @param userId 요청 유저 ID (제외 대상)
     * @param rating 요청 유저의 현재 레이팅
     * @return 선택된 상대방의 Submission
     */
    public Submission chooseOpponent(Long problemId, Long userId, double rating) {
        List<Submission> candidates = submissionQueryRepository.findAllCandidatesDefaultSubmission(problemId, userId);
        if (candidates.isEmpty()) {
            throw OpponentNotFoundException.EXCEPTION;
        }

        List<Long> candidateIds = candidates.stream()
                .map(submission -> submission.getUser().getId())
                .collect(Collectors.toList());
        Map<Long, Double> candidateRatings = userRatingQueryRepository.findCurrentRatings(problemId, candidateIds);

        int bestIdx = selectBestCandidateIndex(candidates, candidateRatings, rating);

        return candidates.get(bestIdx);
    }

    private int selectBestCandidateIndex(List<Submission> candidates, Map<Long, Double> candidateRatings, double myRating) {
        double sigma = calculateSigma(myRating);
        int n = candidates.size();
        int bestIdx = -1;
        double bestVal = Double.NEGATIVE_INFINITY;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < n; i++) {
            Submission submission = candidates.get(i);
            Long candidateId = submission.getUser().getId();
            double candidateRating = candidateRatings.getOrDefault(candidateId, 1500.0);

            double d = Math.abs(myRating - candidateRating);
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
        return bestIdx;

    }

    private double calculateSigma(double rating) {
        if (rating < 500) return -0.2 * rating + 200;
        if (rating < 1500) return -0.05 * rating + 125;
        if (rating < 2500) return -0.04 * rating + 110;
        return 10.0;
    }
}
