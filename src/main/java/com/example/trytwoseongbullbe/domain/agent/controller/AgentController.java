package com.example.trytwoseongbullbe.domain.agent.controller;

import com.example.trytwoseongbullbe.domain.agent.entity.AgentClassifyJob;
import com.example.trytwoseongbullbe.domain.agent.service.AgentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;
    private final ObjectMapper objectMapper;

    @PostMapping(
            value = "/classify",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "문서 분류 (동기)",
            description = """
                    파일 업로드 후 Spring이 FastAPI /classify를 동기로 호출하여
                    최종 JSON payload를 받은 뒤 DB에 저장하고,
                    그 최종 JSON payload를 그대로 반환합니다.
                    """
    )
    public ResponseEntity<JsonNode> classify(
            @Parameter(
                    description = "구매계획서 파일",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(objectMapper.createObjectNode().put("error", "empty file"));
        }

        // ✅ 여기서 FastAPI 호출 완료까지 기다렸다가 최종 JSON을 바로 반환
        JsonNode payload = agentService.classifyAndPersistSync(file);
        return ResponseEntity.ok(payload);
    }

    // ====== GET /classify/{sessionId} : DB에 저장된 최종 payload 그대로 반환 ======
    @GetMapping(
            value = "/classify/{sessionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "문서 분류 결과 조회",
            description = """
                    session_id로 DB에 저장된 payload(JSON)를 반환합니다.
                    - COMPLETED: payload(JSON) 반환
                    - FAILED: error_message 반환
                    """
    )
    public ResponseEntity<Object> getClassifyResult(
            @Parameter(description = "session_id", required = true)
            @PathVariable String sessionId
    ) {
        AgentClassifyJob job = agentService.getClassifyJob(sessionId).orElse(null);

        if (job == null) {
            return ResponseEntity.status(404).body(Map.of("error", "session_id not found"));
        }

        if ("COMPLETED".equals(job.getStatus()) && job.getPayload() != null) {
            try {
                return ResponseEntity.ok(objectMapper.readTree(job.getPayload()));
            } catch (Exception e) {
                return ResponseEntity.ok(Map.of(
                        "session_id", job.getSessionId(),
                        "status", "COMPLETED",
                        "payload_raw", job.getPayload()
                ));
            }
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("session_id", job.getSessionId());
        res.put("file_name", job.getFileName());
        res.put("status", job.getStatus());
        if ("FAILED".equals(job.getStatus())) {
            res.put("error_message", job.getErrorMessage());
        }
        return ResponseEntity.ok(res);
    }

    // ====== 나머지 기존 로직은 그대로 ======

    @PostMapping(
            value = "/validate-template",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> validateTemplate(
            @RequestParam(name = "cntrctCnclsMthdNm") String templateType,
            @RequestParam(name = "days_ago", defaultValue = "7") int daysAgo
    ) {
        if (templateType.isBlank()) {
            return ResponseEntity.badRequest().body("{\"error\":\"cntrctCnclsMthdNm is required\"}");
        }
        String json = agentService.validateTemplate(templateType, daysAgo);
        return ResponseEntity.ok(json);
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> upload(
            @RequestParam(name = "template_id") long templateId,
            @RequestParam(name = "format", defaultValue = "markdown") String format,
            @RequestBody Object request
    ) {
        if (templateId <= 0) {
            return ResponseEntity.badRequest().body("{\"error\":\"template_id must be positive\"}");
        }
        if (request == null) {
            return ResponseEntity.badRequest().body("{\"error\":\"request body is required\"}");
        }
        return ResponseEntity.ok(agentService.upload(request, templateId, format));
    }
}