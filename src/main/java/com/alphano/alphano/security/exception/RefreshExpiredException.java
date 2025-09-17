package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class RefreshExpiredException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new RefreshExpiredException();

    private RefreshExpiredException() {
        super(SecurityErrorCode.REFRESH_EXPIRED);
    }
}
