package com.alphano.alphano.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest (
        @NotBlank
        String identifier,

        @NotBlank
        String password,

        @NotBlank
        String nickname
) {}
