package com.example.trytwoseongbullbe.infrastructure.publicdata;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "public-data")
public record PublicDataApiProperties(String baseUrl, String serviceKey) {
}