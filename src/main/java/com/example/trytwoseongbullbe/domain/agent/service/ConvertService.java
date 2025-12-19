package com.example.trytwoseongbullbe.domain.agent.service;

import com.example.trytwoseongbullbe.domain.agent.client.AgentApiClient;
import com.example.trytwoseongbullbe.domain.agent.dto.ConvertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvertService {

    private final AgentApiClient agentApiClient;

    public ResponseEntity<byte[]> convert(ConvertRequest request) {
        ResponseEntity<byte[]> upstream = agentApiClient.convert(request);
        if (upstream == null || upstream.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        HttpHeaders out = new HttpHeaders();

        // ✅ Content-Type 패스스루
        MediaType ct = upstream.getHeaders().getContentType();
        if (ct != null) {
            out.setContentType(ct);
        }

        // ✅ Content-Disposition 패스스루 (한글 filename*=UTF-8''... 유지)
        String cd = upstream.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        if (cd != null) {
            out.set(HttpHeaders.CONTENT_DISPOSITION, cd);
        }

        // (선택) 캐시 방지 / 프록시 안전
        out.setCacheControl(CacheControl.noStore());
        out.setPragma("no-cache");

        // (선택) Content-Length도 있으면 넣기
        Long len = upstream.getHeaders().getContentLength();
        if (len != null && len > 0) {
            out.setContentLength(len);
        }

        return new ResponseEntity<>(upstream.getBody(), out, upstream.getStatusCode());
    }
}