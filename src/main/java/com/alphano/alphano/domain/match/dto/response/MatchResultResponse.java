package com.alphano.alphano.domain.match.dto.response;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.domain.match.domain.EndReason;
import com.alphano.alphano.domain.match.domain.Match;
import com.alphano.alphano.domain.match.domain.MatchResult;
import com.alphano.alphano.domain.match.domain.MatchStatus;
import com.alphano.alphano.domain.userHistory.domain.UserHistory;

import static com.alphano.alphano.common.consts.AlphanoStatic.ALPHANO_SUBMISSIONS;

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
    public static MatchResultResponse from(Match match, UserHistory history1, UserHistory history2, S3Service s3Service) {
        Agent agent1Dto = Agent.from(history1, match.getAgent1EndReason());
        Agent agent2Dto = Agent.from(history2, match.getAgent2EndReason());

        S3Response log = null;
        if (match.getLogKey() != null && !match.getLogKey().isBlank()) {
            String url = s3Service.createPresignedGetUrl(ALPHANO_SUBMISSIONS, match.getLogKey());
            log = S3Response.of(match.getLogKey(), url);
        }

        return new MatchResultResponse(
                match.getId(),
                match.getStatus(),
                match.getResult(),
                agent1Dto,
                agent2Dto,
                log
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
