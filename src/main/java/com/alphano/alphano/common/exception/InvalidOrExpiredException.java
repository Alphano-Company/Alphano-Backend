package com.alphano.alphano.common.exception;

public class InvalidOrExpiredException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new InvalidOrExpiredException();

    private InvalidOrExpiredException() {
        super(GlobalErrorCode.INVALID_OR_EXPIRED_CODE);
    }
}
