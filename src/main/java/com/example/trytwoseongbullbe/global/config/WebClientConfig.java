package com.example.trytwoseongbullbe.global.config;

import com.example.trytwoseongbullbe.domain.agent.config.AgentApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final AgentApiProperties agentApiProperties;

    @Bean
    @Qualifier("agentWebClient")
    public WebClient agentWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(agentApiProperties.getBaseUrl())
                .build();
    }
}