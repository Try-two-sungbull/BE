package com.example.trytwoseongbullbe.global.auth.controller;

import com.example.trytwoseongbullbe.global.auth.dto.request.SignInRequest;
import com.example.trytwoseongbullbe.global.auth.dto.response.TokenResponse;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "로그인", description = "admin 계정으로 로그인 후 JWT Access Token을 발급합니다.")
    ApiResponse<TokenResponse> signIn(SignInRequest request);
}