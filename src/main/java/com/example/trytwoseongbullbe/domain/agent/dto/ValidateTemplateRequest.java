package com.example.trytwoseongbullbe.domain.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTemplateRequest {

    @Schema(description = "템플릿 유형 (예: 적격심사, 소액수의)", example = "적격심사")
    @JsonProperty("template_type")
    private String templateType;

    @Schema(description = "며칠 전부터 조회할지 (기본 7일)", example = "7", defaultValue = "7")
    @JsonProperty("days_ago")
    private Integer daysAgo; // null이면 서비스에서 7로 세팅
}