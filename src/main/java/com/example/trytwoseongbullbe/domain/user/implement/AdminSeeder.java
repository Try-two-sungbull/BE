package com.example.trytwoseongbullbe.domain.user.implement;

import com.example.trytwoseongbullbe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        userService.seedAdmin();
    }
}