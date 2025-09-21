package com.alphano.alphano.domain.problem.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSummaryQuery {
    private Long problemId;
    private String title;
    private Integer submissionCount;
    private Integer submitterCount;
    private String iconKey;
}
