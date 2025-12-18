package com.example.trytwoseongbullbe.global.auth.controller;

import static com.example.trytwoseongbullbe.global.response.type.ErrorType.INVALID_CREDENTIALS;

import com.example.trytwoseongbullbe.global.auth.dto.request.SignInRequest;
import com.example.trytwoseongbullbe.global.auth.jwt.JwtTokenProvider;
import com.example.trytwoseongbullbe.global.auth.service.AuthService;
import com.example.trytwoseongbullbe.global.exception.CustomException;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private static final String ACCESS = "access_token";
    private static final String REFRESH = "refresh_token";

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    // 🔥 여기 중요
    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site:Lax}")
    private String cookieSameSite;

    @Override
    @PostMapping("/sign-in")
    public ApiResponse<Void> signIn(
            @RequestBody SignInRequest request,
            HttpServletResponse response
    ) {
        AuthService.TokenPair tokens = authService.signInAndIssueTokens(request);

        // access: 1시간
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                createCookie(ACCESS, tokens.accessToken(), Duration.ofHours(1)).toString()
        );

        // refresh: 7일
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                createCookie(REFRESH, tokens.refreshToken(), Duration.ofDays(7)).toString()
        );

        return ApiResponse.success(ErrorType.AUTH_SIGN_IN_SUCCESS, null);
    }

    @Override
    @PostMapping("/refresh")
    public ApiResponse<Void> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = getCookieValue(request, REFRESH);

        if (refreshToken == null || !jwtTokenProvider.validate(refreshToken)) {
            throw new CustomException(INVALID_CREDENTIALS);
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                createCookie(ACCESS, newAccessToken, Duration.ofHours(1)).toString()
        );

        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("/sign-out")
    public ApiResponse<Void> signOut(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie(ACCESS).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie(REFRESH).toString());
        return ApiResponse.success(null);
    }

    private ResponseCookie createCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieSecure)       // ✅ 환경별
                .sameSite(cookieSameSite)   // ✅ 환경별
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    private ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(0)
                .build();
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                String v = cookie.getValue();
                return (v == null || v.isBlank()) ? null : v;
            }
        }
        return null;
    }
}