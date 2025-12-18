package com.example.trytwoseongbullbe.domain.document.controller;

import com.example.trytwoseongbullbe.domain.document.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class DocumentController implements DocumentControllerDocs {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // JSON 업로드
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Long> upload(@RequestBody String json) {
        Long id = documentService.saveJson(json);
        return ResponseEntity.ok(id);
    }

    // JSON 조회
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<String> get(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getJson(id));
    }
}