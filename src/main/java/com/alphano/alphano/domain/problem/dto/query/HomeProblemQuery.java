package com.alphano.alphano.domain.problem.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeProblemQuery {
    private Long problemId;
    private String title;
    private Integer submissionCount;
    private Integer submitterCount;
    private String topPlayer;
    private String description;
    private String iconKey;
}
