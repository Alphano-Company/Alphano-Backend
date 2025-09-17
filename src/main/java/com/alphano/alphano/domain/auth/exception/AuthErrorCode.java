package com.alphano.alphano.domain.auth.exception;

import com.alphano.alphano.common.dto.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.*;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    IDENTIFIER_ALREADY_EXISTS(BAD_REQUEST, "Identifier_Already_Exists", "이미 사용 중인 아이디입니다."),
    NICKNAME_ALREADY_EXISTS(BAD_REQUEST, "Nickname_Already_Exists", "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD(UNAUTHORIZED, "Invalid_Password", "비밀번호가 일치하지 않습니다.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }
}
