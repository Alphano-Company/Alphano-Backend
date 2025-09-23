package com.alphano.alphano.domain.problem.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardQuery {
    private Long userId;
    private String identifier;
    private String profileImageKey;
    private Integer rating;
    private Integer win;
    private Integer lose;
    private Integer draw;
    private Integer winStreak;
    private Integer bestWinStreak;
}