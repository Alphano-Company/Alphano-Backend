package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.dto.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode {
    INVALID_TOKEN(UNAUTHORIZED, "Invalid_Token", "유효하지 않은 토큰입니다."),
    INVALID_SIGNATURE(UNAUTHORIZED, "Invalid_Signature", "유효하지 않은 서명입니다."),
    REFRESH_EXPIRED(UNAUTHORIZED, "Refresh_Expired", "로그인이 만료되었습니다."),
    IDENTIFIER_NOT_FOUND(UNAUTHORIZED, "Identifier_Not_Found", "존재하지 않는 아이디입니다.");

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
