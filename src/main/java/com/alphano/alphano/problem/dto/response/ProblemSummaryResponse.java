package com.alphano.alphano.problem.dto.response;

public record ProblemSummaryResponse (
  Long problemId,
  String title,
  Integer submissionCount,
  Integer submitterCount
){}
