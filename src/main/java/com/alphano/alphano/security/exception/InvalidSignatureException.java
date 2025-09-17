package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class InvalidSignatureException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new InvalidSignatureException();

    private InvalidSignatureException() {
        super(SecurityErrorCode.INVALID_SIGNATURE);
    }
}
