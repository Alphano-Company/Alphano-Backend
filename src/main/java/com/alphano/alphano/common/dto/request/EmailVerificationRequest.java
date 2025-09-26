package com.alphano.alphano.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmailVerificationRequest(
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "인증코드를 입력해주세요.")
        @Pattern(regexp = "\\d+", message = "인증 코드는 숫자 문자열이어야 합니다.")
        @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
        String code
){}