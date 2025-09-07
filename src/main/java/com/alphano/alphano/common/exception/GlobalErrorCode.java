package com.alphano.alphano.common.exception;

import com.alphano.alphano.common.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.INTERNAL_SERVER;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    INTERNAL_SERVER_ERROR(INTERNAL_SERVER, "GLOBAL_500", "서버 오류");

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
