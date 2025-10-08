package com.alphano.alphano.domain.user.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class UserNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
