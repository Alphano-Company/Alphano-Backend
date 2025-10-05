package com.alphano.alphano.domain.problem.dto.response;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.common.util.RatingUtils;
import com.alphano.alphano.domain.problem.dto.query.LeaderboardQuery;

public record LeaderboardResponse (
        Long userId,
        String identifier,
        S3Response profileImage,
        Double rating,
        Integer win,
        Integer lose,
        Integer draw,
        Integer winStreak,
        Integer bestWinStreak
){
    public static LeaderboardResponse from(LeaderboardQuery query, S3Service s3Service) {
        S3Response profileImage = null;
        if (query.getProfileImageKey() != null && !query.getProfileImageKey().isBlank()) {
            String url = s3Service.createPublicUrl(query.getProfileImageKey());
            profileImage = S3Response.of(query.getProfileImageKey(), url);
        }

        Double alphanoRating = RatingUtils.convert(query.getRating());

        return new LeaderboardResponse(
                query.getUserId(),
                query.getIdentifier(),
                profileImage,
                alphanoRating,
                query.getWin(),
                query.getLose(),
                query.getDraw(),
                query.getWinStreak(),
                query.getBestWinStreak()
        );
    }
}
