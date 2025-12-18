package com.example.trytwoseongbullbe.global.exception;

import com.example.trytwoseongbullbe.global.response.ApiResponse;
import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        ErrorType type = e.getErrorType();
        return ResponseEntity
                .status(type.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.error(type));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(
            Exception e,
            HttpServletRequest request
    ) throws Exception {

        String path = request.getRequestURI();

        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")) {
            throw e;
        }

        return ResponseEntity
                .status(ErrorType.INTERNAL_SERVER_ERROR.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR));
    }
}