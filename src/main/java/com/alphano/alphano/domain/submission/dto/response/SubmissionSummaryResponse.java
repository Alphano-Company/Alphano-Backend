package com.alphano.alphano.domain.submission.dto.response;

import com.alphano.alphano.domain.submission.domain.Submission;

import java.time.LocalDateTime;

public record SubmissionSummaryResponse(
       Long submissionId,
       Integer win,
       Integer lose,
       Integer draw,
       Integer codeLength,
       LocalDateTime createdAt,
       boolean isDefault
) {
    public static SubmissionSummaryResponse from(Submission submission) {
        return new SubmissionSummaryResponse(
                submission.getId(),
                submission.getWin(),
                submission.getLose(),
                submission.getDraw(),
                submission.getCodeLength(),
                submission.getCreatedAt(),
                submission.isDefault()
        );
    }
}
