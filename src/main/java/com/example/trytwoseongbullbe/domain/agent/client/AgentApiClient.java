package com.example.trytwoseongbullbe.domain.agent.client;

import com.example.trytwoseongbullbe.domain.agent.config.AgentApiProperties;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AgentApiClient {

    private final WebClient webClient;
    private final AgentApiProperties props;

    public String classify(MultipartFile file) {
        try {
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            return webClient.post()
                    .uri("/classify")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read upload file", e);
        }
    }

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
}