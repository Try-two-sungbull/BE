package com.example.trytwoseongbullbe.domain.reference.business.controller;

import com.example.trytwoseongbullbe.domain.reference.business.dto.request.BusinessSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.business.dto.response.BusinessSimpleResponseDto;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "Reference - Business", description = "공공데이터 기반 업종 조회 API")
public interface BusinessControllerDocs {

    @Operation(
            summary = "업종 단일 조회",
            description = """
                    업종코드(indstrytyCd)로 공공데이터 API에서 업종 정보를 조회합니다.
                    - 프론트 요구사항: data는 배열이 아니라 단일 객체
                    - 반환 필드: indstrytyCd, indstrytyNm, baseLawordNm, baseLawordArtclClauseNm
                    - 조회 결과 없으면 success=false, data=null
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공/실패(조회 결과 없음 포함)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/businesses")
    ResponseEntity<ApiResponse<BusinessSimpleResponseDto>> search(
            @ParameterObject @ModelAttribute BusinessSearchRequestDto req
    );
}