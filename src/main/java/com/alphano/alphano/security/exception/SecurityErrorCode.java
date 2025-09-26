package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.dto.response.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.FORBIDDEN;
import static com.alphano.alphano.common.consts.AlphanoStatic.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode {
    INVALID_TOKEN(UNAUTHORIZED, "Invalid_Token", "유효하지 않은 토큰입니다."),
    INVALID_SIGNATURE(UNAUTHORIZED, "Invalid_Signature", "유효하지 않은 서명입니다."),
    ACCESS_EXPIRED(UNAUTHORIZED, "Access_Expired", "액세스 토큰이 만료되었습니다."),
    REFRESH_EXPIRED(UNAUTHORIZED, "Refresh_Expired", "리프레쉬 토큰이 만료되었습니다."),
    IDENTIFIER_NOT_FOUND(UNAUTHORIZED, "Identifier_Not_Found", "존재하지 않는 아이디입니다."),
    AUTHENTICATION_REQUIRED(UNAUTHORIZED, "Authentication_Required", "인증이 필요합니다."),
    ACCESS_DENIED(FORBIDDEN, "Access_Denied", "접근 권한이 없습니다.")
    ;

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
