package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;
import com.alphano.alphano.domain.submission.exception.SubmissionErrorCode;

public class InvalidTokenException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(SecurityErrorCode.INVALID_TOKEN);
    }
}
