package com.alphano.alphano.domain.auth.dto.response;

public record AccessTokenResponse (
        String accesToken,
        long accessTokenExpiresInMillis
){}
