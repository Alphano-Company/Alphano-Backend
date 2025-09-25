package com.alphano.alphano.common.exception;

import com.alphano.alphano.common.dto.response.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlphanoCodeException extends RuntimeException {
    private BaseErrorCode errorCode;
    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }
}
