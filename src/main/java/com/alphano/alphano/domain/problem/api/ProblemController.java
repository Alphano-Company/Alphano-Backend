package com.alphano.alphano.domain.problem.api;

import com.alphano.alphano.domain.problem.application.ProblemService;
import com.alphano.alphano.domain.problem.dto.response.HomeProblemResponse;
import com.alphano.alphano.domain.problem.dto.response.ProblemDetailResponse;
import com.alphano.alphano.domain.problem.dto.response.ProblemSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping()
    @Operation(summary = "문제 목록 조회", description = "id 오름차순 및 size=10 기본")
    ResponseEntity<Page<ProblemSummaryResponse>> getProblemSummary(
            @ParameterObject
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10)
            Pageable pageable
    ) {
        return ResponseEntity.ok(problemService.getProblemSummary(pageable));
    }

    @GetMapping("/{problemId}")
    @Operation(summary = "문제 상세 조회")
    ResponseEntity<ProblemDetailResponse> getProblemDetail(
            @PathVariable Long problemId
    ) {
        return ResponseEntity.ok(problemService.getProblemDetail(problemId));
    }

    @GetMapping("/popular")
    @Operation(summary = "인기 있는 문제 조회")
    ResponseEntity<List<HomeProblemResponse>> getPopularProblem() {
        return ResponseEntity.ok(problemService.getPopularProblem());
    }

    @GetMapping("/recent")
    @Operation(summary = "최근에 추가된 문제 조회")
    ResponseEntity<HomeProblemResponse> getRecentProblem() {
        return ResponseEntity.ok(problemService.getRecentProblem());
    }
}
