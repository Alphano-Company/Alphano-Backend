package com.alphano.alphano.domain.match.dto.response;

import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.domain.match.domain.EndReason;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.domain.MatchStatus;

public record MatchResultResponse(
        Long matchId,
        MatchStatus status,
        MatchResult result,
        Agent agent1,
        Agent agent2,
        S3Response log
){
    public record Agent (
            Long userId,
            String nickname,
            EndReason endReason,
            double ratingBefore,
            double ratingAfter,
            double delta
    ){}
}
