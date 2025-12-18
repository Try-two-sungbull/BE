package com.example.trytwoseongbullbe.domain.reference.product.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductBodyResponseDto(
        List<ProductItemResponseDto> items,
        Integer numOfRows,
        Integer pageNo,
        Integer totalCount
) {
    public List<ProductItemResponseDto> itemsOrEmpty() {
        return items == null ? List.of() : items;
    }
}