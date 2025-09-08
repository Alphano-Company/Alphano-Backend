package com.alphano.alphano.problem.api;

import com.alphano.alphano.problem.application.ProblemService;
import com.alphano.alphano.problem.dto.response.ProblemSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
