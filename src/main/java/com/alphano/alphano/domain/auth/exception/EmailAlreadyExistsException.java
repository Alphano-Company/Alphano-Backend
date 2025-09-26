package com.alphano.alphano.domain.auth.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class EmailAlreadyExistsException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new EmailAlreadyExistsException();

    private EmailAlreadyExistsException() {
        super(AuthErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
