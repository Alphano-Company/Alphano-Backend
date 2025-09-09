package com.alphano.alphano.match.api;

import com.alphano.alphano.match.application.MatchService;
import com.alphano.alphano.match.dto.request.MatchRequest;
import com.alphano.alphano.match.dto.response.MatchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems/{problemId}/matches")
public class MatchController {

    private final MatchService matchService;

    public ResponseEntity<MatchResponse> createMatch(
            @PathVariable Long problemId,
            @RequestBody @Valid MatchRequest request
    ) {
        MatchResponse response = matchService.create(problemId, request);
        return ResponseEntity.ok(response);
    }
}
