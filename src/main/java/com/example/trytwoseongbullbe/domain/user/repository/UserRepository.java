package com.example.trytwoseongbullbe.domain.user.repository;

import com.example.trytwoseongbullbe.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}