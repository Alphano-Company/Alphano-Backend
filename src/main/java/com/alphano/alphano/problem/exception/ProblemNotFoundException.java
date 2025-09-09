package com.alphano.alphano.problem.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class ProblemNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new ProblemNotFoundException();

    private ProblemNotFoundException() {
        super(ProblemErrorCode.PROBLEM_NOT_FOUND);
    }
}
