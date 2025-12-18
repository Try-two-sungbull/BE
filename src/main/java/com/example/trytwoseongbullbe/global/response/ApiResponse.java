package com.example.trytwoseongbullbe.global.response;

import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import lombok.Builder;

@Builder
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    /* =======================
     * SUCCESS
     * ======================= */

    // ✅ 기본 success: 문자열 "OK" 제거 -> enum OK 사용
    public static <T> ApiResponse<T> success(T data) {
        return success(ErrorType.OK, data);
    }

    // ✅ enum 기반 success
    public static <T> ApiResponse<T> success(ErrorType type, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(type.getCode())
                .message(type.getMessage())
                .data(data)
                .build();
    }

    /* =======================
     * ERROR
     * ======================= */

    // ✅ enum 기반 error (문자열 code/message 제거)
    public static ApiResponse<?> error(ErrorType type) {
        return ApiResponse.builder()
                .success(false)
                .code(type.getCode())
                .message(type.getMessage())
                .data(null)
                .build();
    }

    // (호환용) 기존 시그니처 유지하고 싶으면 남겨도 됨
    public static ApiResponse<?> error(String code, String message) {
        return ApiResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}