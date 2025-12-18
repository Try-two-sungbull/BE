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

    public static <T> ApiResponse<T> success(T data) {
        return success(ErrorType.OK, data);
    }

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

    public static <T> ApiResponse<T> error(ErrorType type) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(type.getCode())
                .message(type.getMessage())
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}