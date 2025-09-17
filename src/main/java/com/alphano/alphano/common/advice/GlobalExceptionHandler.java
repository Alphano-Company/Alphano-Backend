package com.alphano.alphano.common.advice;

import com.alphano.alphano.common.dto.ErrorReason;
import com.alphano.alphano.common.dto.ErrorResponse;
import com.alphano.alphano.common.exception.AlphanoCodeException;
import com.alphano.alphano.common.exception.BaseErrorCode;
import com.alphano.alphano.common.exception.GlobalErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlphanoCodeException.class)
    public ResponseEntity<ErrorResponse> alphanoCodeExceptionHandler(AlphanoCodeException e, HttpServletRequest request) {
        BaseErrorCode code = e.getErrorCode();
        ErrorReason errorReason = code.getErrorReason();
        ErrorResponse errorResponse = ErrorResponse.from(errorReason, request.getRequestURL().toString());
        return ResponseEntity.status(HttpStatus.valueOf(errorReason.getStatus())).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException e, HttpServletRequest request) {
        ErrorReason errorReason = ErrorReason.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("UNAUTHORIZED_401")
                .reason("인증이 필요합니다.")
                .build();
        ErrorResponse errorResponse = ErrorResponse.from(errorReason, request.getRequestURL().toString());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler({ AccessDeniedException.class, AuthorizationDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception e, HttpServletRequest request) {
        ErrorReason errorReason = ErrorReason.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .code("FORBIDDEN_403")
                .reason("권한이 없습니다.")
                .build();
        ErrorResponse errorResponse = ErrorResponse.from(errorReason, request.getRequestURL().toString());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        ErrorReason errorReason = ErrorReason.builder()
                .status(HttpStatus.CONFLICT.value())
                .code("DATA_INTEGRITY_VIOLATION")
                .reason("데이터 무결성 위반이 발생했습니다.")
                .build();

        ErrorResponse errorResponse = ErrorResponse.from(errorReason, request.getRequestURL().toString());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object>  handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ErrorReason errorReason = ErrorReason.builder()
                .status(status.value())
                .code("VALIDATION_400")
                .reason(message)
                .build();

        HttpServletRequest servletRequest = ((org.springframework.web.context.request.ServletWebRequest) request).getRequest();
        String path = servletRequest.getRequestURL().toString();
        ErrorResponse errorResponse = ErrorResponse.from(errorReason, path);
        return ResponseEntity.status(HttpStatus.valueOf(errorReason.getStatus()))
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request)
            throws IOException {
        log.error("INTERNAL SERVER ERROR", e);
        GlobalErrorCode internalServerError = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(
                internalServerError.getStatus(),
                internalServerError.getCode(),
                internalServerError.getReason(),
                request.getRequestURL().toString()
        );
        return ResponseEntity.status(HttpStatus.valueOf(internalServerError.getStatus()))
                .body(errorResponse);
    }
}
