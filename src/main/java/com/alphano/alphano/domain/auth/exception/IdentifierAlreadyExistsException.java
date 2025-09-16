package com.alphano.alphano.domain.auth.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;
import com.alphano.alphano.security.exception.SecurityErrorCode;

public class IdentifierAlreadyExistsException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new IdentifierAlreadyExistsException();

    private IdentifierAlreadyExistsException() {
        super(AuthErrorCode.IDENTIFIER_ALREADY_EXISTS);
    }
}
