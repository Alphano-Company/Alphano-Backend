package com.alphano.alphano.common.exception;

public class InternalServerError extends AlphanoCodeException {
    public static final AlphanoCodeException EXCEPTION = new InternalServerError();
    private InternalServerError() {
        super(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
