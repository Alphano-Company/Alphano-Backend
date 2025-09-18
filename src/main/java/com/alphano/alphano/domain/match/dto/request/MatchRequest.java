package com.alphano.alphano.domain.match.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MatchRequest(
        @NotNull
        @Schema(description = "제출 ID", example = "1")
        Long submissionId
){

}
