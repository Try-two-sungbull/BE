package com.example.trytwoseongbullbe.domain.agent.service;

import com.example.trytwoseongbullbe.domain.agent.client.AgentApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentApiClient agentApiClient;

    public String classify(MultipartFile file) {
        return agentApiClient.classify(file);
    }

    public String validateTemplate(String templateType, Integer daysAgo) {
        int safeDaysAgo = (daysAgo == null ? 7 : daysAgo);
        return agentApiClient.validateTemplate(templateType, safeDaysAgo);
    }
}