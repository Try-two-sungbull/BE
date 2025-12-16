package com.example.trytwoseongbullbe.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequest(
        @Schema(example = "admin")
        String username,
        @Schema(example = "1234")
        String password
) {
}