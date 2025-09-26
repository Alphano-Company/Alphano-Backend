package com.alphano.alphano.domain.submission.exception;

import com.alphano.alphano.common.dto.response.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.*;

@Getter
@AllArgsConstructor
public enum SubmissionErrorCode implements BaseErrorCode {
    SUBMISSION_NOT_FOUND(NOT_FOUND, "Submission_Not_Found", "존재하지 않는 제출입니다."),
    SUBMISSION_PROBLEM_MISMATCH(BAD_REQUEST, "Submission_Problem_Mismatch", "제출이 해당 문제에 속하지 않습니다."),
    SUBMISSION_CODE_KEY_MISSING(BAD_REQUEST, "Submission_CodeKey_Missing", "제출에 code key가 누락되었습니다."),
    SUBMISSION_FORBIDDEN(FORBIDDEN, "Submission_Forbidden",  "해당 제출에 접근할 권한이 없습니다.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }
}
