package com.example.trytwoseongbullbe.global.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "JWT Access Token")
        String accessToken
) {
}