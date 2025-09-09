package com.alphano.alphano.match.dto.request;

public record MatchRequest(
        Long problemId,
        Integer randomSeed,
        Long agent1Id  // 로그인한 유저
){

}
