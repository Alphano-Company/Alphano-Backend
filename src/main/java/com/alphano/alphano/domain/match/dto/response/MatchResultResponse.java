package com.alphano.alphano.domain.match.dto.response;

import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.domain.match.domain.EndReason;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.userHistory.domain.UserHistory;

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
            String identifier,
            EndReason endReason,
            double ratingBefore,
            double ratingAfter,
            double delta
    ){
        public static Agent from(UserHistory history, EndReason endReason) {
            return new Agent(
                    history.getUser().getId(),
                    history.getUser().getIdentifier(),
                    endReason,
                    history.getRatingBefore(),
                    history.getRatingAfter(),
                    history.getRatingDelta()
            );
        }
    }

    /**
     * MatchStatus == COMPLETED
     */
    public static MatchResultResponse from(Match match, UserHistory history1, UserHistory history2) {
        Agent agent1Dto = Agent.from(history1, match.getAgent1EndReason());
        Agent agent2Dto = Agent.from(history2, match.getAgent2EndReason());

        return new MatchResultResponse(
                match.getId(),
                match.getStatus(),
                match.getResult(),
                agent1Dto,
                agent2Dto,
                null // S3 로그 정보
        );
    }

    /**
     * MatchStatus == QUEUED || RUNNING || FAILED
     */
    public static MatchResultResponse ofStatus(Match match) {
        return new MatchResultResponse(
                match.getId(),
                match.getStatus(),
                null,
                null,
                null,
                null
        );
    }
}
