package com.alphano.alphano.domain.match.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class MatchNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new MatchNotFoundException();

    private MatchNotFoundException() {
        super(MatchErrorCode.MATCH_NOT_FOUND);
    }
}
