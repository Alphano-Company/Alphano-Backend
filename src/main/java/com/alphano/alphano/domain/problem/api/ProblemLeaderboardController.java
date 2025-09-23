package com.alphano.alphano.domain.problem.api;

import com.alphano.alphano.domain.problem.application.ProblemLeaderboardService;
import com.alphano.alphano.domain.problem.application.ProblemService;
import com.alphano.alphano.domain.problem.dto.response.LeaderboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems")
public class ProblemLeaderboardController {
    private final ProblemLeaderboardService problemLeaderboardService;

    @GetMapping("/{problemId}/leaders")
    @Operation(summary = "문제별 리더보드 조회")
    ResponseEntity<Page<LeaderboardResponse>> getLeaderboard(
            @PathVariable Long problemId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(problemLeaderboardService.getLeaderboard(problemId, pageable));
    }
}
