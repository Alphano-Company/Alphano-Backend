package com.alphano.alphano.domain.match.dto;

public record MatchJobMessage (
        Long problemId,
        Long matchId,
        int randomSeed,
        Agent agent1,
        Agent agent2
){
    public record Agent (
            Long userId,
            Long submissionId,
            String language,
            String codeKey
    ){}
}
