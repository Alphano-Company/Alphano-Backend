package com.alphano.alphano.domain.submission.dto.response;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.domain.submission.domain.Submission;

import java.time.LocalDateTime;

import static com.alphano.alphano.common.consts.AlphanoStatic.ALPHANO_SUBMISSIONS;

public record SubmissionDetailResponse(
       Long submissionId,
       Integer win,
       Integer lose,
       Integer draw,
       String language,
       Integer codeLength,
       LocalDateTime createdAt,
       boolean isDefault,
       S3Response code
) {
    public static SubmissionDetailResponse from(Submission submission, S3Service s3Service) {
        S3Response code = null;
        if (submission.getCodeKey() != null && !submission.getCodeKey().isBlank()) {
            String url = s3Service.createPresignedGetUrl(ALPHANO_SUBMISSIONS, submission.getCodeKey());
            code = S3Response.of(submission.getCodeKey(), url);
        }

        return new SubmissionDetailResponse(
                submission.getId(),
                submission.getWin(),
                submission.getLose(),
                submission.getDraw(),
                submission.getLanguage(),
                submission.getCodeLength(),
                submission.getCreatedAt(),
                submission.isDefault(),
                code
        );
    }
}
