package com.alphano.alphano.domain.match.api;

import com.alphano.alphano.domain.match.application.MatchService;
import com.alphano.alphano.domain.match.dto.response.MatchResponse;
import com.alphano.alphano.domain.match.dto.response.MatchResultResponse;
import com.alphano.alphano.security.principal.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/problems/{problemId}/matches")
    @Operation(
            summary = "SNS에 매치 토픽 발행",
            description = "SQS의 큐에 매치 요청이 쌓임"
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MatchResponse> createMatch(
            @PathVariable Long problemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        MatchResponse response = matchService.create(problemId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/matches/{matchId}")
    @Operation(summary = "매치 결과 조회")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MatchResultResponse> getMatchResult(@PathVariable Long matchId) {
        MatchResultResponse response = matchService.getMatchResult(matchId);
        return ResponseEntity.ok(response);
    }
}
