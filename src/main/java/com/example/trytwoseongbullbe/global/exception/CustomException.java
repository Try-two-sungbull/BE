package com.example.trytwoseongbullbe.global.exception;

import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}