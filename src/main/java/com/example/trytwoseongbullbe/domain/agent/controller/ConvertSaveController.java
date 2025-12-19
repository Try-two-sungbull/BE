package com.example.trytwoseongbullbe.domain.agent.controller;

import com.example.trytwoseongbullbe.domain.agent.dto.ConvertSaveRequest;
import com.example.trytwoseongbullbe.domain.agent.dto.ConvertSaveResponse;
import com.example.trytwoseongbullbe.domain.agent.service.ConvertSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class ConvertSaveController {

    private final ConvertSaveService convertSaveService;

    @PostMapping(
            value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConvertSaveResponse> save(
            @Valid @RequestBody ConvertSaveRequest request
    ) {
        Long id = convertSaveService.save(request);
        return ResponseEntity.ok(new ConvertSaveResponse(id));
    }
}