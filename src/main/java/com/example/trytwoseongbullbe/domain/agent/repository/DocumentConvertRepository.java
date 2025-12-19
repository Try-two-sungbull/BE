package com.example.trytwoseongbullbe.domain.agent.repository;

import com.example.trytwoseongbullbe.domain.agent.entity.DocumentConvert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentConvertRepository
        extends JpaRepository<DocumentConvert, Long> {
}