package com.alphano.alphano.submission.exception;

import com.alphano.alphano.common.exception.AlphanoCodeException;

public class SubmissionProblemMismatchException extends AlphanoCodeException{
    public static final AlphanoCodeException EXCEPTION = new SubmissionProblemMismatchException();

    private SubmissionProblemMismatchException() {
        super(SubmissionErrorCode.SUBMISSION_PROBLEM_MISMATCH);
    }
}
