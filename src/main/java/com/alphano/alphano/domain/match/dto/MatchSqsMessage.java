package com.alphano.alphano.domain.match.dto;

import com.alphano.alphano.domain.match.domain.EndReason;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.domain.MatchStatus;

public record MatchSqsMessage (
    Long matchId,
    Long problemId,
    String logKey,
    MatchStatus status,     // COMPLETED, FAILED
    MatchResult result,     // DRAW, AGENT1_WIN, AGENT2_WIN
    EndReason agent1Reason, // CE, RE, TLE, OK, INTERRUPTED
    EndReason agent2Reason,
    Long randomSeed,
    Long agent1UserId,
    Long agent2UserId
){}
