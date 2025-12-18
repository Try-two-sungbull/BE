package com.example.trytwoseongbullbe.domain.reference.business.service;

import com.example.trytwoseongbullbe.domain.reference.business.dto.request.BusinessSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.business.dto.response.BusinessItemResponseDto;
import com.example.trytwoseongbullbe.domain.reference.business.dto.response.BusinessSimpleResponseDto;
import com.example.trytwoseongbullbe.domain.reference.business.implement.BusinessApiClient;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BusinessService {

    private final BusinessApiClient businessApiClient;

    public BusinessService(BusinessApiClient businessApiClient) {
        this.businessApiClient = businessApiClient;
    }

    // (기존 유지) 리스트 조회
    public List<BusinessItemResponseDto> search(BusinessSearchRequestDto req) {
        long start = System.currentTimeMillis();
        BusinessSearchRequestDto normalized = normalize(req);

        try {
            return businessApiClient.search(normalized);
        } finally {
            log.info("[BusinessService.search] indstrytyCd={}, pageNo={}, numOfRows={}, took={}ms",
                    normalized.indstrytyCd(),
                    normalized.pageNoOrDefault(),
                    normalized.numOfRowsOrDefault(),
                    System.currentTimeMillis() - start);
        }
    }

    // (기존 유지) 내부적으로 첫번째 값 필요할 때
    public Optional<BusinessItemResponseDto> findFirst(BusinessSearchRequestDto req) {
        long start = System.currentTimeMillis();
        BusinessSearchRequestDto normalized = normalize(req);

        try {
            return businessApiClient.findFirst(normalized);
        } finally {
            log.info("[BusinessService.findFirst] indstrytyCd={}, pageNo={}, numOfRows={}, took={}ms",
                    normalized.indstrytyCd(),
                    normalized.pageNoOrDefault(),
                    normalized.numOfRowsOrDefault(),
                    System.currentTimeMillis() - start);
        }
    }

    // ✅ (추가) 프론트 요구: 단일 객체 + 필요한 필드만
    public Optional<BusinessSimpleResponseDto> searchOne(BusinessSearchRequestDto req) {
        long start = System.currentTimeMillis();
        BusinessSearchRequestDto normalized = normalize(req);

        try {
            return businessApiClient.findFirst(normalized)
                    .map(it -> new BusinessSimpleResponseDto(
                            it.indstrytyCd(),
                            it.indstrytyNm(),
                            it.baseLawordNm(),
                            it.baseLawordArtclClauseNm()
                    ));
        } finally {
            log.info("[BusinessService.searchOne] indstrytyCd={}, pageNo={}, numOfRows={}, took={}ms",
                    normalized.indstrytyCd(),
                    normalized.pageNoOrDefault(),
                    normalized.numOfRowsOrDefault(),
                    System.currentTimeMillis() - start);
        }
    }

    private BusinessSearchRequestDto normalize(BusinessSearchRequestDto req) {
        return new BusinessSearchRequestDto(
                req.indstrytyCd(),
                req.pageNoOrDefault(),
                req.numOfRowsOrDefault()
        );
    }
}