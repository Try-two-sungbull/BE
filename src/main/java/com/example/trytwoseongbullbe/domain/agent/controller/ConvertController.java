package com.example.trytwoseongbullbe.domain.agent.controller;

import com.example.trytwoseongbullbe.domain.agent.dto.ConvertRequest;
import com.example.trytwoseongbullbe.domain.agent.service.ConvertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConvertController {

    private final ConvertService convertService;

    @PostMapping(
            value = "/convert",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "HTML을 PDF/DOCX/HWP로 변환",
            description = "Spring이 요청을 받아 FastAPI(/api/v1/convert)를 호출하고, 변환된 파일을 다운로드 응답으로 그대로 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "변환 성공(파일 다운로드)",
            content = @Content(mediaType = "application/octet-stream"))
    public ResponseEntity<byte[]> convert(@Valid @RequestBody ConvertRequest request) {
        return convertService.convert(request);
    }
}