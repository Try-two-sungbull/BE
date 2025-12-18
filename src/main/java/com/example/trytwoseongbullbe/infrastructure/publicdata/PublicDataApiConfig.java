package com.example.trytwoseongbullbe.infrastructure.publicdata;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PublicDataApiProperties.class)
public class PublicDataApiConfig {
}