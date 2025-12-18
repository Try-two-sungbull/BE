package com.example.trytwoseongbullbe.domain.document.repository;


import com.example.trytwoseongbullbe.domain.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}