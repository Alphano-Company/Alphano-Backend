package com.alphano.alphano.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest (
        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        @Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하여야 합니다.")
        @Pattern(
                regexp = "^[a-zA-Z0-9]+$",
                message = "아이디는 영문자와 숫자만 사용할 수 있습니다."
        )
        String identifier,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하여야 합니다.")
        @Pattern(
                regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).*",
                message = "비밀번호는 영문자와 숫자를 반드시 포함해야 하며 공백은 사용할 수 없습니다."
        )
        String password,

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email
) {}
