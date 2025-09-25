package com.alphano.alphano.common.dto.response;

public record EmailVerificationResponse (
        String verifiedToken,
        long expiresInSeconds
){}
