package com.example.trytwoseongbullbe.domain.reference.product.service;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductItemResponseDto;
import com.example.trytwoseongbullbe.domain.reference.product.implement.ProductApiClient;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductApiClient productApiClient;

    public ProductService(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;
    }

    public List<ProductItemResponseDto> search(ProductSearchRequestDto req) {
        ProductSearchRequestDto normalized = normalize(req);
        return productApiClient.search(normalized);
    }

    public Optional<ProductItemResponseDto> findFirst(ProductSearchRequestDto req) {
        ProductSearchRequestDto normalized = normalize(req);
        return productApiClient.findFirst(normalized);
    }

    private ProductSearchRequestDto normalize(ProductSearchRequestDto req) {
        return new ProductSearchRequestDto(
                req.dtilPrdctClsfcNo(),
                req.pageNoOrDefault(),
                req.numOfRowsOrDefault()
        );
    }
}