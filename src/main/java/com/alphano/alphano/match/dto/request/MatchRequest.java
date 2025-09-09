package com.alphano.alphano.match.dto.request;


import jakarta.validation.constraints.NotNull;

public record MatchRequest(
        @NotNull Long agent1Id, // 로그인한 유저
        @NotNull Long submissionId
){

}
