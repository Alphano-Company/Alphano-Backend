package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class IdentifierNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new IdentifierNotFoundException();

    private IdentifierNotFoundException() {
        super(SecurityErrorCode.IDENTIFIER_NOT_FOUND);
    }
}
