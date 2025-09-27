package com.alphano.alphano.domain.match.dto.response;

import com.alphano.alphano.domain.match.domain.MatchStatus;

public record MatchResponse (
        Long problemId,
        Long matchId,
        Long opponentId,
        int randomSeed,
        MatchStatus status
){
    public static MatchResponse of(Long problemId, Long matchId, Long opponentId, int randomSeed, MatchStatus status) {
        return new MatchResponse(
                problemId,
                matchId,
                opponentId,
                randomSeed,
                status
        );
    }
}
