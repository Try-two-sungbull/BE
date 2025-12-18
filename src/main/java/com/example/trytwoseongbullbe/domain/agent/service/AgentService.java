package com.example.trytwoseongbullbe.domain.agent.service;

import com.example.trytwoseongbullbe.domain.agent.client.AgentApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentApiClient agentApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
}