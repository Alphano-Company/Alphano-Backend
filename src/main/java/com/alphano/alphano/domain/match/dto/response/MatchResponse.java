package com.alphano.alphano.domain.match.dto.response;

import com.alphano.alphano.domain.match.domain.MatchStatus;

public record MatchResponse (
        Long problemId,
        Long opponentId,
        int randomSeed,
        MatchStatus status
){}
