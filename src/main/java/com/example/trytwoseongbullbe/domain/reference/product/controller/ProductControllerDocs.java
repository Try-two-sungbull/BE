package com.example.trytwoseongbullbe.domain.reference.product.controller;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductItemResponseDto;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "Reference - Product", description = "공공데이터 기반 품명 조회 API")
public interface ProductControllerDocs {

    @Operation(
            summary = "세부품명 목록 조회",
            description = """
                    세부품명번호(dtilPrdctClsfcNo)를 입력하면 공공데이터 API를 통해 품명 목록을 조회합니다.
                    - 공공데이터 원본 item 리스트를 반환합니다.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/products")
    ResponseEntity<ApiResponse<List<ProductItemResponseDto>>> search(
            @ParameterObject @ModelAttribute ProductSearchRequestDto req
    );
}