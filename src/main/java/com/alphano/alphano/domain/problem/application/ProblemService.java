package com.alphano.alphano.domain.problem.application;

import com.alphano.alphano.domain.problem.dao.ProblemRepository;
import com.alphano.alphano.domain.problem.dto.response.ProblemSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {
    private final ProblemRepository problemRepository;

    private static final int MAX_PAGE_SIZE = 50;

    /**
     * 문제 목록 조회
     * @return
     */
    public Page<ProblemSummaryResponse> getProblemSummary(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);
        Sort sort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Direction.ASC, "id")
                : pageable.getSort();
        PageRequest pageRequest = PageRequest.of(
                Math.max(0, pageable.getPageNumber()),
                pageSize,
                sort
        );

        return problemRepository.findAllSummary(pageable);
    }
}
