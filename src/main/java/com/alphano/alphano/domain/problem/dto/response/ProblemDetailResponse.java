package com.alphano.alphano.domain.problem.dto.response;

public record ProblemDetailResponse (
        Long problemId,
        String title,
        String content,
        String inputFormat,
        String outputFormat,
        int submissionCount,
        int submitterCount
) {}