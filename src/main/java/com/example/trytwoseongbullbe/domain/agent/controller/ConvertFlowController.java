package com.example.trytwoseongbullbe.domain.agent.controller;


import com.example.trytwoseongbullbe.domain.agent.dto.ConvertCreateRequest;
import com.example.trytwoseongbullbe.domain.agent.dto.ConvertCreateResponse;
import com.example.trytwoseongbullbe.domain.agent.service.ConvertFlowService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class ConvertFlowController {

    private final ConvertFlowService convertFlowService;

    // 1) HTML 저장 → id 리턴
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConvertCreateResponse> create(@Valid @RequestBody ConvertCreateRequest req) {
        Long id = convertFlowService.create(req);
        return ResponseEntity.ok(new ConvertCreateResponse(id));
    }

    // 2) id + format으로 다운로드
    // 프론트: POST /convert/download?id=123&format=pdf
    @PostMapping(value = "/download")
    public ResponseEntity<byte[]> download(
            @RequestParam Long id,
            @RequestParam @Pattern(regexp = "pdf|docx") String format
    ) {
        return convertFlowService.download(id, format);
    }
}