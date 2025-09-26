package com.alphano.alphano.common.dto.response;

public record EmailVerificationResponse (
        String token,
        long ttlSeconds
){}
