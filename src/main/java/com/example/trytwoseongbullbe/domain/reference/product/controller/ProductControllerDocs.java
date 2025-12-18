package com.example.trytwoseongbullbe.domain.reference.product.controller;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductSimpleResponseDto;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "Reference - Product", description = "공공데이터 기반 품명 조회 API")
public interface ProductControllerDocs {

    @Operation(
            summary = "세부품명 단일 조회",
            description = """
                    세부품명번호(dtilPrdctClsfcNo)로 공공데이터 API에서 품명 정보를 조회합니다.
                    
                    - 프론트 요구사항: data는 배열이 아니라 단일 객체
                    - 반환 필드: dtilPrdctClsfcNo, dtilPrdctClsfcNoNm
                    - 조회 결과 없으면 success=false, data=null
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "조회 결과 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/products")
    ResponseEntity<ApiResponse<ProductSimpleResponseDto>> search(
            @ParameterObject @ModelAttribute ProductSearchRequestDto req
    );
}