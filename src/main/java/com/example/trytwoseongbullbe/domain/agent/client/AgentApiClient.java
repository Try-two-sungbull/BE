package com.example.trytwoseongbullbe.domain.agent.client;

import com.example.trytwoseongbullbe.domain.agent.dto.ConvertRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AgentApiClient {

    private final @Qualifier("agentWebClient") WebClient webClient;

    /**
     * FastAPI: POST /classify (multipart)
     */
    public String classify(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String filename = file.getOriginalFilename();

            return classify(bytes, filename);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read upload file", e);
        }
    }

    /**
     * FastAPI: POST /validate-template?cntrctCnclsMthdNm=...&days_ago=...
     */
    public String validateTemplate(String templateType, int daysAgo) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/validate-template")
                        .queryParam("cntrctCnclsMthdNm", templateType)
                        .queryParam("days_ago", daysAgo)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * FastAPI: POST /upload?template_id=...&format=... Body: UploadDocumentRequest(JSON)
     */
    public String upload(String requestJson, long templateId, String format) {
        return webClient.post()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/upload")
                            .queryParam("template_id", templateId);
                    if (format != null && !format.isBlank()) {
                        b.queryParam("format", format);
                    }
                    return b.build();
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestJson)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * FastAPI: POST /classify (multipart) - byte[] 버전 NOTE: - bodyValue(Map) 쓰면 multipart encoder 안 타서 boundary 누락될 수
     * 있음 - fromMultipartData로 보내면 boundary 포함된 정상 multipart로 전송됨
     */
    public String classify(byte[] fileBytes, String originalFilename) {
        String safeFilename = (originalFilename == null || originalFilename.isBlank())
                ? "upload.bin"
                : originalFilename;

        ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return safeFilename;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        return webClient.post()
                .uri("/classify")
                // contentType을 굳이 박지 말고 encoder가 boundary 포함해서 잡게 두는게 안전함
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(msg -> new RuntimeException(
                                        "FastAPI error [" + response.statusCode() + "]: " + msg
                                ))
                )
                .bodyToMono(String.class)
                .block();
    }

    /**
     * FastAPI: POST /api/v1/convert (json) -> binary file (pdf/docx/hwp) 응답 헤더(Content-Type, Content-Disposition)를 그대로
     * 받아서 Spring이 프록시로 내려줄 수 있게 ResponseEntity로 반환
     */
    public ResponseEntity<byte[]> convert(ConvertRequest request) {
        return webClient.post()
                // ✅ convert만 절대경로로 호출 (baseUrl 무시)
                .uri("https://ai.hack.bluerack.org/api/v1/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(
                        MediaType.APPLICATION_PDF,
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
                        MediaType.parseMediaType("application/x-hwp"),
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(msg -> new RuntimeException(
                                        "FastAPI error [" + response.statusCode() + "]: " + msg
                                ))
                )
                .toEntity(byte[].class)
                .block();
    }
}