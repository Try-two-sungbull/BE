package com.example.trytwoseongbullbe.domain.reference.business.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessSimpleResponseDto(
        @JsonProperty("indstrytyCd") String indstrytyCd,
        @JsonProperty("indstrytyNm") String indstrytyNm,
        @JsonProperty("baseLawordNm") String baseLawordNm,
        @JsonProperty("baseLawordArtclClauseNm") String baseLawordArtclClauseNm
) {
}