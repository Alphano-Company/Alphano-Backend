package com.alphano.alphano.domain.problem.dto.response;

import com.alphano.alphano.domain.problem.domain.Problem;

public record ProblemDetailResponse (
        Long problemId,
        String title,
        String content,
        String inputFormat,
        String outputFormat,
        int submissionCount,
        int submitterCount
) {
    public static ProblemDetailResponse from(Problem problem) {
        return new ProblemDetailResponse(
                problem.getId(),
                problem.getTitle(),
                problem.getContent(),
                problem.getInputFormat(),
                problem.getOutputFormat(),
                problem.getSubmissionCount(),
                problem.getSubmitterCount()
        );
    }
}