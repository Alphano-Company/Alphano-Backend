package com.alphano.alphano.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        @Schema(description = "아이디", example = "amily9011")
        String identifier,

        @NotBlank
        @Schema(description = "비밀번호", example = "1234")
        String password
) {}
