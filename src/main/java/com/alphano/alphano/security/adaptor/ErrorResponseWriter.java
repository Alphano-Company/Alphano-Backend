package com.alphano.alphano.security.adaptor;

import com.alphano.alphano.common.dto.response.ErrorReason;
import com.alphano.alphano.common.exception.BaseErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, BaseErrorCode code) throws IOException {
        ErrorReason errorReason = code.getErrorReason();
        response.setStatus(errorReason.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorReason);
    }
}
