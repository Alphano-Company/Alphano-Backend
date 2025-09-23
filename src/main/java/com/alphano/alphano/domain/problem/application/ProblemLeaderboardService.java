package com.alphano.alphano.domain.problem.application;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.domain.problem.dao.ProblemQueryRepository;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.dto.query.LeaderboardQuery;
import com.alphano.alphano.domain.problem.dto.response.LeaderboardResponse;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import com.alphano.alphano.domain.userRating.dao.UserRatingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemLeaderboardService {
    private final S3Service s3Service;
    private final UserRatingQueryRepository userRatingQueryRepository;
    private final ProblemRepository problemRepository;

    /**
     * 리더보드 조회
     * @param problemId
     * @param pageable
     * @return
     */
    public Page<LeaderboardResponse> getLeaderboard(Long problemId, Pageable pageable) {
        if (!problemRepository.existsById(problemId)) {
            throw ProblemNotFoundException.EXCEPTION;
        }

        Page<LeaderboardQuery> result = userRatingQueryRepository.getLeaderboard(problemId, pageable);
        return result.map(query -> LeaderboardResponse.from(query, s3Service));
    }
}
