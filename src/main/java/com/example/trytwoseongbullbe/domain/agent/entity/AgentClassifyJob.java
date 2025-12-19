package com.example.trytwoseongbullbe.domain.agent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agent_classify_jobs")
@Getter
@Setter
@NoArgsConstructor
public class AgentClassifyJob {

    @Id
    @Column(name = "session_id", length = 36)
    private String sessionId; // UUID string (FastAPI session_id를 그대로 PK로 사용)

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // PROCESSING / COMPLETED / FAILED

    @Lob
    @Column(name = "payload", columnDefinition = "json")
    private String payload; // FastAPI 최종 JSON 통째로 저장

    @Lob
    @Column(name = "error_message", columnDefinition = "longtext")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static AgentClassifyJob newProcessing(String sessionId, String fileName) {
        AgentClassifyJob job = new AgentClassifyJob();
        job.sessionId = sessionId;
        job.fileName = fileName;
        job.status = "PROCESSING";
        job.createdAt = LocalDateTime.now();
        job.updatedAt = LocalDateTime.now();
        return job;
    }

    public void markCompleted(String payloadJson) {
        this.status = "COMPLETED";
        this.payload = payloadJson;
        this.errorMessage = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String errorMessage) {
        this.status = "FAILED";
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }
}