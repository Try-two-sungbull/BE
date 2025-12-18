package com.example.trytwoseongbullbe.domain.reference.product.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductItemResponseDto(
        @JsonProperty("prdctClsfcNo") String prdctClsfcNo,
        @JsonProperty("prdctClsfcNoNm") String prdctClsfcNoNm,
        @JsonProperty("prdctClsfcNoEngNm") String prdctClsfcNoEngNm,

        @JsonProperty("dtilPrdctClsfcNo") String dtilPrdctClsfcNo,
        @JsonProperty("dtilPrdctClsfcNoNm") String dtilPrdctClsfcNoNm,
        @JsonProperty("dtilPrdctClsfcNoEngNm") String dtilPrdctClsfcNoEngNm,

        @JsonProperty("dtilPrdctClsfcNoNum") String dtilPrdctClsfcNoNum
) {
}