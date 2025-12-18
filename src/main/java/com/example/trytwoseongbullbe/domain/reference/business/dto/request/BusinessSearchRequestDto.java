package com.example.trytwoseongbullbe.domain.reference.business.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record BusinessSearchRequestDto(

        @Schema(description = "업종코드", example = "4608")
        @NotBlank
        String indstrytyCd,

        @Schema(description = "페이지 번호", example = "1", defaultValue = "1")
        Integer pageNo,

        @Schema(description = "한 페이지 조회 건수", example = "1", defaultValue = "1")
        Integer numOfRows
) {
    public int pageNoOrDefault() {
        return pageNo == null ? 1 : pageNo;
    }

    public int numOfRowsOrDefault() {
        return numOfRows == null ? 1 : numOfRows;
    }
}