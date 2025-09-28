package com.alphano.alphano.domain.submission.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class SubmissionNotReadyException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new SubmissionNotReadyException();

    private SubmissionNotReadyException() {
        super(SubmissionErrorCode.SUBMISSION_NOT_READY);
    }
}
