package com.example.trytwoseongbullbe.domain.document.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface DocumentControllerDocs {

    @Operation(
            summary = "문서 JSON 업로드",
            description = "문서 분석 결과 JSON 전체를 그대로 DB에 저장하고 문서 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "저장 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "1")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(mediaType = "application/json")
    )
    ResponseEntity<Long> upload(
            @RequestBody(
                    required = true,
                    description = "저장할 JSON payload",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "session_id": "uuid",
                                              "file_name": "good.pdf",
                                              "status": "classified"
                                            }
                                            """
                            )
                    )
            )
            String json
    );

    @Operation(
            summary = "문서 JSON 조회",
            description = "문서 ID로 저장된 JSON payload를 그대로 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "session_id": "uuid",
                                      "status": "classified"
                                    }
                                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "문서 없음",
            content = @Content(mediaType = "application/json")
    )
    ResponseEntity<String> get(
            @Parameter(description = "문서 ID", example = "1")
            @PathVariable Long id
    );
}