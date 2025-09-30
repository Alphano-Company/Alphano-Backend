package com.alphano.alphano.domain.submission.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class SubmissionCodeObjectNotFoundException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new SubmissionCodeObjectNotFoundException();

    private SubmissionCodeObjectNotFoundException() {
        super(SubmissionErrorCode.SUBMISSION_CODE_OBJECT_NOT_FOUND);
    }
}
