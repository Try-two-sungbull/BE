package com.example.trytwoseongbullbe.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequest(
        @Schema(example = "admin")
        String username,
        @Schema(example = "12345")
        String password
) {
}