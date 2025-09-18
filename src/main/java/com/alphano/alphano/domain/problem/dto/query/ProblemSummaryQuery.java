package com.alphano.alphano.domain.problem.dto.query;

public record ProblemSummaryQuery(
        Long problemId,
        String title,
        int submissionCount,
        int submitterCount,
        String iconKey
) {
}
