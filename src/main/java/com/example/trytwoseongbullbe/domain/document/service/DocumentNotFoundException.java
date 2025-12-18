package com.example.trytwoseongbullbe.domain.document.service;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super("document not found: " + id);
    }
}