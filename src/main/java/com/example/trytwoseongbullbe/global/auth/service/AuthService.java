package com.example.trytwoseongbullbe.global.auth.service;

import static com.example.trytwoseongbullbe.global.response.type.ErrorType.ADMIN_NOT_INITIALIZED;
import static com.example.trytwoseongbullbe.global.response.type.ErrorType.INVALID_CREDENTIALS;

import com.example.trytwoseongbullbe.domain.user.entity.User;
import com.example.trytwoseongbullbe.domain.user.implement.UserImpl;
import com.example.trytwoseongbullbe.global.auth.dto.request.SignInRequest;
import com.example.trytwoseongbullbe.global.auth.dto.response.TokenResponse;
import com.example.trytwoseongbullbe.global.auth.jwt.JwtTokenProvider;
import com.example.trytwoseongbullbe.global.config.props.AdminProperties;
import com.example.trytwoseongbullbe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserImpl userImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminProperties adminProperties;

    public TokenResponse signIn(SignInRequest request) {
        String adminUsername = adminProperties.getUsername();

        // ✅ admin username만 허용
        if (!adminUsername.equals(request.username())) {
            throw new CustomException(INVALID_CREDENTIALS);
        }

        User admin = userImpl.getByUsernameOrNull(adminUsername);
        if (admin == null) {
            throw new CustomException(ADMIN_NOT_INITIALIZED);
        }

        if (!passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
            throw new CustomException(INVALID_CREDENTIALS);
        }

        return new TokenResponse(jwtTokenProvider.generateToken(admin.getUsername()));
    }
}