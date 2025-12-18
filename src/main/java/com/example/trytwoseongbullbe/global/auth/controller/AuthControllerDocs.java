package com.example.trytwoseongbullbe.global.auth.controller;

import com.example.trytwoseongbullbe.global.auth.dto.request.SignInRequest;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(
            summary = "로그인",
            description = "admin 계정으로 로그인 후 JWT Access Token / Refresh Token을 HttpOnly 쿠키로 발급합니다. " +
                    "(access_token, refresh_token)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그인 성공 (Set-Cookie로 access_token, refresh_token 내려감)",
            headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Set-Cookie",
                            description = "access_token=<JWT>; HttpOnly; Secure; SameSite=None; Path=/"
                    ),
                    @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Set-Cookie",
                            description = "refresh_token=<JWT>; HttpOnly; Secure; SameSite=None; Path=/"
                    )
            },
            content = @Content
    )
    ApiResponse<Void> signIn(SignInRequest request, HttpServletResponse response);

    @Operation(
            summary = "토큰 재발급",
            description = "refresh_token 쿠키가 유효하면 access_token 쿠키를 새로 발급합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "재발급 성공 (Set-Cookie로 access_token 갱신)",
            headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Set-Cookie",
                            description = "access_token=<NEW_JWT>; HttpOnly; Secure; SameSite=None; Path=/"
                    )
            },
            content = @Content
    )
    ApiResponse<Void> refresh(HttpServletRequest request, HttpServletResponse response);

    @Operation(
            summary = "로그아웃",
            description = "access_token, refresh_token 쿠키를 만료시켜 로그아웃 처리합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공 (Set-Cookie로 access_token, refresh_token 삭제)",
            headers = {
                    @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Set-Cookie",
                            description = "access_token=; Max-Age=0; Expires=...; HttpOnly; Secure; SameSite=None; Path=/"
                    ),
                    @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Set-Cookie",
                            description = "refresh_token=; Max-Age=0; Expires=...; HttpOnly; Secure; SameSite=None; Path=/"
                    )
            },
            content = @Content
    )
    ApiResponse<Void> signOut(HttpServletResponse response);
}