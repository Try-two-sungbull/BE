package com.example.trytwoseongbullbe.global.auth.controller;

import com.example.trytwoseongbullbe.global.auth.dto.request.SignInRequest;
import com.example.trytwoseongbullbe.global.auth.dto.response.TokenResponse;
import com.example.trytwoseongbullbe.global.auth.service.AuthService;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/sign-in")
    public ApiResponse<TokenResponse> signIn(@RequestBody SignInRequest request) {
        return ApiResponse.success("SUCCESS", "로그인 성공", authService.signIn(request));
    }
}