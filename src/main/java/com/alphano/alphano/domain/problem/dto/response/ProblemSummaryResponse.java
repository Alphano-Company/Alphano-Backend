package com.alphano.alphano.domain.problem.dto.response;

import com.alphano.alphano.common.dto.response.S3Response;
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
                query.getProblemId(),
                query.getTitle(),
                query.getSubmissionCount(),
                query.getSubmitterCount(),
                iconImage
        );
    }
}
