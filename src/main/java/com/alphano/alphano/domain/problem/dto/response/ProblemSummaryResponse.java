package com.alphano.alphano.domain.problem.dto.response;

import com.alphano.alphano.common.dto.S3Response;
import com.alphano.alphano.domain.problem.dto.query.ProblemSummaryQuery;

public record ProblemSummaryResponse (
  Long problemId,
  String title,
  Integer submissionCount,
  Integer submitterCount,
  S3Response iconImage
){
    public static ProblemSummaryResponse from(ProblemSummaryQuery query, S3Response iconImage) {
        return new ProblemSummaryResponse(
                query.problemId(),
                query.title(),
                query.submissionCount(),
                query.submitterCount(),
                iconImage
        );
    }
}
