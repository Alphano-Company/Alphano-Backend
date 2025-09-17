package com.alphano.alphano.domain.submission.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class SubmissionCodeKeyMissingException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new SubmissionCodeKeyMissingException();

    private SubmissionCodeKeyMissingException() {
        super(SubmissionErrorCode.SUBMISSION_CODE_KEY_MISSING);
    }
}
