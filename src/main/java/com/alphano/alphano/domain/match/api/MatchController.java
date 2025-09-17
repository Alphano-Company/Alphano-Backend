package com.alphano.alphano.domain.match.api;

import com.alphano.alphano.domain.match.application.MatchService;
import com.alphano.alphano.domain.match.dto.request.MatchRequest;
import com.alphano.alphano.domain.match.dto.response.MatchResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems/{problemId}/matches")
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @Operation(summary = "SNS에 매치 토픽 발행", description = "SQS의 큐에 매치 요청이 쌓임")
    public ResponseEntity<MatchResponse> createMatch(
            @PathVariable Long problemId,
            @RequestBody @Valid MatchRequest request
    ) {
        MatchResponse response = matchService.create(problemId, request);
        return ResponseEntity.ok(response);
    }
}
