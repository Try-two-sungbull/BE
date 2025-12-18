package com.example.trytwoseongbullbe.global.exception;

import com.example.trytwoseongbullbe.global.response.ApiResponse;
import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
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
                .body(ApiResponse.error(type));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) throws Exception {
        String path = request.getRequestURI();

        // ✅ springdoc(swagger) 관련 경로는 예외 포맷을 건드리면 깨짐 → 그대로 던짐
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")) {
            throw e;
        }

        // ✅ 여기 정책은 선택:
        // - 지금처럼 무조건 400으로 보내고 싶으면 BAD_REQUEST
        // - 일반적으로는 500이 더 맞음
        return ResponseEntity
                .status(ErrorType.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR));
    }
}