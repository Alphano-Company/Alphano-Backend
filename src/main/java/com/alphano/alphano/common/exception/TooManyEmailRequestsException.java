package com.alphano.alphano.common.exception;

public class TooManyEmailRequestsException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new TooManyEmailRequestsException();

    private TooManyEmailRequestsException() {
        super(GlobalErrorCode.TOO_MANY_EMAIL_REQUESTS);
    }
}
