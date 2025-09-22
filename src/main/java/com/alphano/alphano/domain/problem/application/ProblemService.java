package com.alphano.alphano.domain.problem.application;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.S3Response;
import com.alphano.alphano.domain.problem.dao.ProblemQueryRepository;
import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.domain.Problem;
import com.alphano.alphano.domain.problem.dto.query.HomeProblemQuery;
import com.alphano.alphano.domain.problem.dto.query.ProblemSummaryQuery;
import com.alphano.alphano.domain.problem.dto.response.HomeProblemResponse;
import com.alphano.alphano.domain.problem.dto.response.ProblemDetailResponse;
import com.alphano.alphano.domain.problem.dto.response.ProblemSummaryResponse;
import com.alphano.alphano.domain.problem.exception.ProblemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {
    private final S3Service s3Service;
    private final ProblemRepository problemRepository;
    private final ProblemQueryRepository problemQueryRepository;

    private static final int MAX_PAGE_SIZE = 50;

    /**
     * 문제 목록 조회
     * @return
     */
    public Page<ProblemSummaryResponse> getProblemSummary(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);
        int page = Math.max(0, pageable.getPageNumber());

        Sort sort = pageable.getSortOr(Sort.by(Sort.Order.asc("id")));
        PageRequest pageRequest = PageRequest.of(page, pageSize, sort);

        Page<ProblemSummaryQuery> result = problemQueryRepository.findAllSummary(pageRequest);

        return result.map(row -> {
            String iconKey = row.getIconKey();
            S3Response iconImage = null;
            if (iconKey != null && !iconKey.isBlank()) {
                String url = s3Service.createPublicUrl(iconKey);
                iconImage = S3Response.of(iconKey, url);
            }
            return ProblemSummaryResponse.from(row, iconImage);
        });
    }

    /**
     * 문제 상세 조회
     * @param problemId
     * @return
     */
    public ProblemDetailResponse getProblemDetail(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> ProblemNotFoundException.EXCEPTION);
        return ProblemDetailResponse.from(problem);
    }

    /**
     * 인기 있는 문제 목록 조회
     * @return 인기 문제 상위 3개 리스트
     */
    public List<HomeProblemResponse> getPopularProblem() {
        int limit = 3;  // 상위 인기 문제 개수
        return problemQueryRepository.findPopularProblems(limit).stream()
                .map(query -> HomeProblemResponse.from(query, s3Service))
                .toList();
    }

    /**
     * 최근에 추가된 문제 조회
     * @return 최근에 추가된 문제 1개 조회
     */
    public HomeProblemResponse getRecentProblem() {
        HomeProblemQuery query = problemQueryRepository.findRecentProblem();
        return HomeProblemResponse.from(query, s3Service);
    }
}
