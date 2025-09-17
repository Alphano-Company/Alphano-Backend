package com.alphano.alphano.domain.auth.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class InvalidPasswordException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new InvalidPasswordException();

    private InvalidPasswordException() {
        super(AuthErrorCode.INVALID_PASSWORD);
    }
}
