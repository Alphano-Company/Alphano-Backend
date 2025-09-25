package com.alphano.alphano.domain.problem.dto.response;

import com.alphano.alphano.common.application.S3Service;
import com.alphano.alphano.common.dto.response.S3Response;
import com.alphano.alphano.domain.problem.dto.query.HomeProblemQuery;

public record HomeProblemResponse(
        Long problemId,
        String title,
        Integer submissionCount,
        Integer submitterCount,
        String topPlayer,
        String description,
        S3Response iconImage
) {
    public static HomeProblemResponse from(HomeProblemQuery query, S3Service s3Service) {
        S3Response iconImage = null;
        if (query.getIconKey() != null && !query.getIconKey().isBlank()) {
            String url = s3Service.createPublicUrl(query.getIconKey());
            iconImage = S3Response.of(query.getIconKey(), url);
        }

        return new HomeProblemResponse(
                query.getProblemId(),
                query.getTitle(),
                query.getSubmissionCount(),
                query.getSubmitterCount(),
                query.getTopPlayer(),
                query.getDescription(),
                iconImage
        );
    }
}
