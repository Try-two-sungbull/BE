package com.example.trytwoseongbullbe.domain.agent.service;

import com.example.trytwoseongbullbe.domain.agent.client.AgentApiClient;
import com.example.trytwoseongbullbe.domain.agent.dto.ConvertCreateRequest;
import com.example.trytwoseongbullbe.domain.agent.dto.ConvertRequest;
import com.example.trytwoseongbullbe.domain.agent.entity.DocumentConvert;
import com.example.trytwoseongbullbe.domain.agent.repository.DocumentConvertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConvertFlowService {

    private final DocumentConvertRepository repository;
    private final AgentApiClient agentApiClient;

    @Transactional
    public Long create(ConvertCreateRequest req) {
        DocumentConvert saved = repository.save(DocumentConvert.of(req.filename(), req.html()));
        return saved.getId();
    }

    public ResponseEntity<byte[]> download(Long id, String format) {
        DocumentConvert doc = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("convert id not found: " + id));

        // DB에서 꺼낸 값으로 FastAPI 요청 JSON 구성
        ConvertRequest fastReq = new ConvertRequest(
                doc.getHtml(),
                format,
                doc.getFilename()
        );

        // FastAPI 호출
        ResponseEntity<byte[]> upstream = agentApiClient.convert(fastReq);
        if (upstream == null || upstream.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        // ✅ 헤더 패스스루 (Swagger처럼 다운로드 링크 뜨게)
        HttpHeaders out = new HttpHeaders();

        MediaType ct = upstream.getHeaders().getContentType();
        if (ct != null) {
            out.setContentType(ct);
        }

        String cd = upstream.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        if (cd != null) {
            out.set(HttpHeaders.CONTENT_DISPOSITION, cd);
        }

        out.setCacheControl(CacheControl.noStore());
        out.setPragma("no-cache");

        long len = upstream.getHeaders().getContentLength();
        if (len > 0) {
            out.setContentLength(len);
        }

        return new ResponseEntity<>(upstream.getBody(), out, upstream.getStatusCode());
    }
}