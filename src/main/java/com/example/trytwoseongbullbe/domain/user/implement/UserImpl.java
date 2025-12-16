package com.example.trytwoseongbullbe.domain.user.implement;

import com.example.trytwoseongbullbe.domain.user.entity.User;
import com.example.trytwoseongbullbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserImpl {

    private final UserRepository userRepository;

    public User getByUsernameOrNull(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}