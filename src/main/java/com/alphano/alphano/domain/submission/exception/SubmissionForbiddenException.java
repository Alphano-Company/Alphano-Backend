package com.alphano.alphano.domain.submission.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class SubmissionForbiddenException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new SubmissionForbiddenException();

    private SubmissionForbiddenException() {
        super(SubmissionErrorCode.SUBMISSION_FORBIDDEN);
    }
}
