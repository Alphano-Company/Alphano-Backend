package com.alphano.alphano.domain.submission.api;

import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.domain.submission.application.SubmissionService;
import com.alphano.alphano.domain.submission.dto.request.AddSubmissionRequest;
import com.alphano.alphano.domain.submission.dto.response.SubmissionDetailResponse;
import com.alphano.alphano.domain.submission.dto.response.SubmissionSummaryResponse;
import com.alphano.alphano.security.principal.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/submissions/{submissionId}")
    @Operation(summary = "제출 상세 조회")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SubmissionDetailResponse> getSubmissionDetail(
            @PathVariable Long submissionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(submissionService.getSubmissionDetail(userId, submissionId));
    }

    @PatchMapping("/submissions/{submissionId}")
    @Operation(summary = "대표 코드로 설정")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateSubmission(
            @PathVariable Long submissionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        submissionService.setDefaultSubmission(userId, submissionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/problems/{problemId}/submissions")
    @Operation(summary = "코드 추가")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<S3Response> addSubmission(
            @PathVariable Long problemId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            AddSubmissionRequest request
    ) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(submissionService.addSubmission(problemId, userId, request));
    }

    @PatchMapping("/submissions/{submissionId}/finalize")
    @Operation(summary = "s3에 코드 객체 업로드 확정")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addSubmission(
            @PathVariable Long submissionId
    ) {
        submissionService.finalizeSubmission(submissionId);
        return ResponseEntity.ok().build();
    }
}
