package com.alphano.alphano.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest (
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "닉네임은 영문자와 숫자로 이루어진 3~20자여야 합니다.")
        String identifier,

        @NotBlank
        String password
) {}
