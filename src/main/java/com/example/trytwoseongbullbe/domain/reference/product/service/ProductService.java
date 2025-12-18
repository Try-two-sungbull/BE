package com.example.trytwoseongbullbe.domain.reference.product.service;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductItemResponseDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductSimpleResponseDto;
import com.example.trytwoseongbullbe.domain.reference.product.implement.ProductApiClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductApiClient productApiClient;

    public Optional<ProductSimpleResponseDto> searchOne(ProductSearchRequestDto req) {
        long start = System.currentTimeMillis();
        ProductSearchRequestDto normalized = normalize(req);

        try {
            Optional<ProductItemResponseDto> first = productApiClient.findFirst(normalized);
            return first.map(it -> new ProductSimpleResponseDto(it.dtilPrdctClsfcNo(), it.dtilPrdctClsfcNoNm()));
        } finally {
            log.info("[ProductService.searchOne] dtilPrdctClsfcNo={}, pageNo={}, numOfRows={}, took={}ms",
                    normalized.dtilPrdctClsfcNo(),
                    normalized.pageNoOrDefault(),
                    normalized.numOfRowsOrDefault(),
                    System.currentTimeMillis() - start);
        }
    }

    private ProductSearchRequestDto normalize(ProductSearchRequestDto req) {
        return new ProductSearchRequestDto(
                req.dtilPrdctClsfcNo(),
                req.pageNoOrDefault(),
                req.numOfRowsOrDefault()
        );
    }
}