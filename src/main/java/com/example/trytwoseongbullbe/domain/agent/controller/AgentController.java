package com.example.trytwoseongbullbe.domain.agent.controller;


import com.example.trytwoseongbullbe.domain.agent.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    @PostMapping(
            value = "/classify",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> classify(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"empty file\"}");
        }
        String json = agentService.classify(file);
        return ResponseEntity.ok(json);
    }

    @PostMapping(
            value = "/validate-template",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "템플릿 검증",
            description = """
                    템플릿 검증 API
                    
                    1. 나라장터에서 해당 유형의 최신 공고문 조회
                    2. 우리 템플릿 로드
                    3. 비교 Agent로 차이점 분석
                    4. 변경사항 있으면 신버전 템플릿 반환
                    
                    Args: cntrctCnclsMthdNm: 공고 유형(적격심사, 소액수의 등)
                          days_ago: 조회 기간(기본 7일)
                    """
    )
    public ResponseEntity<String> validateTemplate(
            @Parameter(description = "공고 유형 (예: 적격심사, 소액수의)", required = true)
            @RequestParam(name = "cntrctCnclsMthdNm") String templateType,

            @Parameter(description = "며칠 전부터 조회할지 (기본 7일)")
            @RequestParam(name = "days_ago", defaultValue = "7") int daysAgo
    ) {
        if (templateType.isBlank()) {
            return ResponseEntity.badRequest().body("{\"error\":\"cntrctCnclsMthdNm is required\"}");
        }
        String json = agentService.validateTemplate(templateType, daysAgo);
        return ResponseEntity.ok(json);
    }

}