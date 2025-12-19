package com.example.trytwoseongbullbe.domain.agent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document_converts")
@Getter
@Setter
@NoArgsConstructor
public class DocumentConvert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename", nullable = false, length = 255)
    private String filename;

    @Lob
    @Column(name = "html", nullable = false, columnDefinition = "longtext")
    private String html;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static DocumentConvert of(String filename, String html) {
        DocumentConvert e = new DocumentConvert();
        e.filename = filename;
        e.html = html;
        e.createdAt = LocalDateTime.now();
        return e;
    }
}