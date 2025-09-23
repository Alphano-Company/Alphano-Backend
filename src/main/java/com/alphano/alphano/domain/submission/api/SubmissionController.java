package com.alphano.alphano.domain.submission.api;

import com.alphano.alphano.domain.submission.application.SubmissionService;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import com.alphano.alphano.security.principal.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/problems/{problemId}/submissions")
    @Operation(summary = "문제별 유저 제출 조회")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<SubmissionSummaryResponse>> getAllSubmissions(
            @PathVariable Long problemId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(submissionService.getAllSubmissions(userId, problemId, pageable));
    }
}
