package com.alphano.alphano.domain.problem.exception;

import com.alphano.alphano.common.dto.response.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ProblemErrorCode implements BaseErrorCode {
    PROBLEM_NOT_FOUND(NOT_FOUND, "Problem_404", "존재하지 않는 문제입니다.");

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
