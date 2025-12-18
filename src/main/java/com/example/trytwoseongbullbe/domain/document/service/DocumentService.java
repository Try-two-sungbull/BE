package com.example.trytwoseongbullbe.domain.document.service;

import com.example.trytwoseongbullbe.domain.document.entity.Document;
import com.example.trytwoseongbullbe.domain.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Transactional
    public Long saveJson(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("json is blank");
        }
        Document saved = documentRepository.save(new Document(json));
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public String getJson(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("document not found: " + id))
                .getPayload();
    }
}