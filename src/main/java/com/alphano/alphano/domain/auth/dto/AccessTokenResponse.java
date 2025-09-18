package com.alphano.alphano.domain.auth.dto;

public record AccessTokenResponse (
        String accesToken,
        long accessTokenExpiresInMillis
){}
