package com.example.trytwoseongbullbe.domain.document.dto.response;


public class DocumentUploadResponseDto {
    private Long id;

    public DocumentUploadResponseDto() {
    }

    public DocumentUploadResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}