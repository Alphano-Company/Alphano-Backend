package com.alphano.alphano.security.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class AccessExpiredException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new AccessExpiredException();

    private AccessExpiredException() {
        super(SecurityErrorCode.ACCESS_EXPIRED);
    }
}
