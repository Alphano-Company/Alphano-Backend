package com.alphano.alphano.common.exception;

import com.alphano.alphano.domain.submission.exception.SubmissionErrorCode;

public class UnableToSendEmailException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new UnableToSendEmailException();

    private UnableToSendEmailException() {
        super(GlobalErrorCode.UNABLE_TO_SEND_EMAIL);
    }
}
