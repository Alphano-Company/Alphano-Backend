package com.alphano.alphano.domain.auth.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class NicknameAlreadyExistsException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new NicknameAlreadyExistsException();

    private NicknameAlreadyExistsException() {
        super(AuthErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}
