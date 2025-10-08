package com.alphano.alphano.domain.match.dto;

import com.alphano.alphano.domain.match.domain.EndReason;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MatchSqsMessage (
    @JsonProperty("match_id") Long matchId,
    @JsonProperty("problem_id") Long problemId,
    @JsonProperty("log_key") String logKey,
    MatchStatus status,
    MatchResult result,
    @JsonProperty("agent1_reason") EndReason agent1Reason,
    @JsonProperty("agent2_reason") EndReason agent2Reason,
    @JsonProperty("random_seed") Integer randomSeed,
    @JsonProperty("agent1_userid") Long agent1UserId,
    @JsonProperty("agent2_userid") Long agent2UserId
){}
