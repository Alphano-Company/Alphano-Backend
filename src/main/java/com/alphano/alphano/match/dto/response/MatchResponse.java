package com.alphano.alphano.match.dto.response;

import com.alphano.alphano.match.domain.MatchStatus;

public record MatchResponse (
        Long problemId,
        Long opponentId,
        int randomSeed,
        MatchStatus status
){}
