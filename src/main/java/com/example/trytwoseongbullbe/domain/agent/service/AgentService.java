package com.example.trytwoseongbullbe.domain.agent.service;

import com.example.trytwoseongbullbe.domain.agent.client.AgentApiClient;
import com.example.trytwoseongbullbe.domain.agent.entity.AgentClassifyJob;
import com.example.trytwoseongbullbe.domain.agent.repository.AgentClassifyJobRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentApiClient agentApiClient;
    private final AgentClassifyJobRepository jobRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ====== 기존 메소드 유지 ======
    public String classify(MultipartFile file) {
        return agentApiClient.classify(file);
    }

    public String validateTemplate(String templateType, Integer daysAgo) {
        int safeDaysAgo = (daysAgo == null ? 7 : daysAgo);
        return agentApiClient.validateTemplate(templateType, safeDaysAgo);
    }

    public String upload(Object uploadRequestDtoOrMap, long templateId, String format) {
        try {
            String json = objectMapper.writeValueAsString(uploadRequestDtoOrMap);
            return agentApiClient.upload(json, templateId, format);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize upload request", e);
        }
    }

    // ====== ✅ (수정) 동기 classify: FastAPI 최종 JSON 받고 -> 그 JSON만 DB 저장 ======
    public JsonNode classifyAndPersistSync(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("empty file");
        }

        String fileName = (file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());

        // 요청 끝나면 MultipartFile 위험하니 bytes로 복사
        final byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }

        // 1) FastAPI 호출 (최종 JSON 올 때까지 block)
        String raw = agentApiClient.classify(bytes, fileName);
        System.out.println("===== FastAPI raw response =====");
        System.out.println(raw);
        System.out.println("================================");

        // 2) JSON 검증 + 정규화 (MySQL JSON 컬럼에 안전)
        final JsonNode payloadNode;
        try {
            payloadNode = objectMapper.readTree(raw);
        } catch (Exception e) {
            // FastAPI가 JSON이 아닌 걸 뱉으면 DB payload(json) 저장 불가 -> FAILED로 기록
            String fallbackSessionId = java.util.UUID.randomUUID().toString();
            AgentClassifyJob failed = AgentClassifyJob.newProcessing(fallbackSessionId, fileName);
            failed.markFailed("FastAPI returned non-JSON response: " + raw);
            jobRepository.save(failed);
            throw new RuntimeException("FastAPI returned non-JSON response", e);
        }

        // 3) session_id는 FastAPI가 준 걸 그대로 PK로 사용
        JsonNode sidNode = payloadNode.get("session_id");
        if (sidNode == null || sidNode.asText().isBlank()) {
            // session_id가 없으면 설계 자체가 깨짐 -> FAILED 기록
            String fallbackSessionId = java.util.UUID.randomUUID().toString();
            AgentClassifyJob failed = AgentClassifyJob.newProcessing(fallbackSessionId, fileName);
            failed.markFailed("FastAPI response missing session_id");
            jobRepository.save(failed);
            throw new RuntimeException("FastAPI response missing session_id");
        }

        String sessionId = sidNode.asText();

        // 4) DB 저장 (payload = “최종 JSON만”)
        AgentClassifyJob job = jobRepository.findById(sessionId).orElseGet(() -> {
            AgentClassifyJob j = AgentClassifyJob.newProcessing(sessionId, fileName);
            // createdAt/updatedAt 세팅은 newProcessing에서 됨
            return j;
        });

        // 파일명도 FastAPI JSON 안에 있으면 그걸 우선 (없으면 업로드 파일명)
        JsonNode fn = payloadNode.get("file_name");
        job.setFileName(fn != null && !fn.asText().isBlank() ? fn.asText() : fileName);

        // status는 내부적으로 COMPLETED로 관리 (payload 안의 status="classified"는 payload에 그대로 있음)
        job.setUpdatedAt(LocalDateTime.now());
        job.markCompleted(payloadNode.toString()); // ✅ 유효 JSON 문자열만 저장

        jobRepository.save(job);

        // 5) 컨트롤러로 최종 JSON 그대로 반환
        return payloadNode;
    }

    // ====== (GET용 조회) 기존 유지 ======
    public Optional<AgentClassifyJob> getClassifyJob(String sessionId) {
        return jobRepository.findById(sessionId);
    }
}