package com.alphano.alphano.domain.match.exception;

import com.alphano.alphano.common.dto.response.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.alphano.alphano.common.consts.AlphanoStatic.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements BaseErrorCode {
    OPPONENT_NOT_FOUND(NOT_FOUND, "Opponent_Not_Found", "상대방을 찾을 수 없습니다."),
    MATCH_NOT_FOUND(NOT_FOUND, "Match_Not_Found", "해당 매치를 찾을 수 없습니다.");

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
