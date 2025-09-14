package com.alphano.alphano.domain.match.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class OpponentNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new OpponentNotFoundException();

    private OpponentNotFoundException() {
        super(MatchErrorCode.OPPONENT_NOT_FOUND);
    }
}
