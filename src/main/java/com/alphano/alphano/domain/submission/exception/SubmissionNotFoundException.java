package com.alphano.alphano.domain.submission.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class SubmissionNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new SubmissionNotFoundException();

    private SubmissionNotFoundException() {
        super(SubmissionErrorCode.SUBMISSION_NOT_FOUND);
    }
}
