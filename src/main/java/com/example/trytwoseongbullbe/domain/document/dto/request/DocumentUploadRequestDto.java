package com.example.trytwoseongbullbe.domain.document.dto.request;


public class DocumentUploadRequestDto {
    private String html;

    public DocumentUploadRequestDto() {
    }

    public DocumentUploadRequestDto(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}