package com.example.trytwoseongbullbe.domain.reference.business.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessItemResponseDto(

        @JsonProperty("indstrytyUseYn")
        String indstrytyUseYn,

        @JsonProperty("indstrytyClsfcCd")
        String indstrytyClsfcCd,

        @JsonProperty("indstrytyClsfcNm")
        String indstrytyClsfcNm,

        @JsonProperty("indstrytyCd")
        String indstrytyCd,

        @JsonProperty("indstrytyNm")
        String indstrytyNm,

        @JsonProperty("baseLawordNm")
        String baseLawordNm,

        @JsonProperty("baseLawordArtclClauseNm")
        String baseLawordArtclClauseNm,

        @JsonProperty("baseLawordUrl")
        String baseLawordUrl,

        @JsonProperty("rltnRgltCntnts")
        String rltnRgltCntnts,

        @JsonProperty("inclsnLcns")
        String inclsnLcns,

        @JsonProperty("indstrytyRgstDt")
        String indstrytyRgstDt,

        @JsonProperty("indstrytyChgDt")
        String indstrytyChgDt
) {
}