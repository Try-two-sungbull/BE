package com.example.trytwoseongbullbe.domain.user.service;

import com.example.trytwoseongbullbe.domain.user.entity.User;
import com.example.trytwoseongbullbe.domain.user.implement.UserImpl;
import com.example.trytwoseongbullbe.global.config.props.AdminProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserImpl userImpl;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Transactional
    public void seedAdmin() {
        String username = adminProperties.getUsername();
        String rawPassword = adminProperties.getPassword();

        User admin = userImpl.getByUsernameOrNull(username);

        if (admin == null) {
            userImpl.save(User.builder()
                    .username(username)
                    .passwordHash(passwordEncoder.encode(rawPassword))
                    .build());
            return;
        }

        // ✅ 비번이 바뀌었으면 업데이트
        if (!passwordEncoder.matches(rawPassword, admin.getPasswordHash())) {
            admin.changePasswordHash(passwordEncoder.encode(rawPassword));
        }
    }
}