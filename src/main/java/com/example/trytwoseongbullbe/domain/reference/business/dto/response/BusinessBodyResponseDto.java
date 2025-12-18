package com.example.trytwoseongbullbe.domain.reference.business.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessBodyResponseDto(
        @JsonProperty("items") List<BusinessItemResponseDto> items,
        @JsonProperty("totalCount") Integer totalCount,
        @JsonProperty("numOfRows") Integer numOfRows,
        @JsonProperty("pageNo") Integer pageNo
) {
    public List<BusinessItemResponseDto> itemsOrEmpty() {
        return items == null ? List.of() : items;
    }
}