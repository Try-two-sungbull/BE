package com.example.trytwoseongbullbe.domain.agent.service;


import com.example.trytwoseongbullbe.domain.agent.dto.ConvertSaveRequest;
import com.example.trytwoseongbullbe.domain.agent.entity.DocumentConvert;
import com.example.trytwoseongbullbe.domain.agent.repository.DocumentConvertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConvertSaveService {

    private final DocumentConvertRepository repository;

    @Transactional
    public Long save(ConvertSaveRequest request) {
        DocumentConvert saved = repository.save(
                DocumentConvert.of(request.filename(), request.html())
        );
        return saved.getId(); // ✅ 여기서 바로 id
    }
}