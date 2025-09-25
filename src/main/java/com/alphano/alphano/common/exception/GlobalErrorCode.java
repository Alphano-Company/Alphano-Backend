package com.alphano.alphano.common.exception;

import com.alphano.alphano.common.dto.response.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.*;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    INTERNAL_SERVER_ERROR(INTERNAL_SERVER, "GLOBAL_500", "서버 오류"),

    // Email
    UNABLE_TO_SEND_EMAIL(BAD_GATEWAY, "Unable_To_Send_Email", "메일 전송에 실패했습니다."),
    TOO_MANY_EMAIL_REQUESTS(TOO_MANY_REQUESTS, "Too_Many_Email_Requests", "요청이 너무 많습니다. 잠시 후 다시 시도해 주세요."),
    INVALID_OR_EXPIRED_CODE(UNPROCESSABLE_ENTITY, "Invalid_Or_Expired_Code", "인증 코드가 만료되었거나 올바르지 않습니다.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .status(status)
                .code(code)
                .reason(reason)
                .build();
    }
}
