package com.alphano.alphano.domain.problem.dto.response;

public record ProblemSummaryResponse (
  Long problemId,
  String title,
  Integer submissionCount,
  Integer submitterCount
){}
