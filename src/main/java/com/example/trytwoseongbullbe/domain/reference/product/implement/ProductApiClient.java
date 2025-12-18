package com.example.trytwoseongbullbe.domain.reference.product.implement;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductBodyResponseDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductItemResponseDto;
import com.example.trytwoseongbullbe.infrastructure.publicdata.PublicDataApiClient;
import com.example.trytwoseongbullbe.infrastructure.publicdata.PublicDataEnvelope;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProductApiClient {

    private static final String PATH =
            "/1230000/ao/ThngListInfoService/getThngPrdnmLocplcAccotListInfoInfoPrdnmSearch";

    private final PublicDataApiClient publicDataApiClient;

    public ProductApiClient(PublicDataApiClient publicDataApiClient) {
        this.publicDataApiClient = publicDataApiClient;
    }

    public List<ProductItemResponseDto> search(ProductSearchRequestDto req) {
        Map<String, Object> params = new HashMap<>();
        params.put("dtilPrdctClsfcNo", req.dtilPrdctClsfcNo());
        params.put("pageNo", req.pageNoOrDefault());
        params.put("numOfRows", req.numOfRowsOrDefault());

        PublicDataEnvelope<ProductBodyResponseDto> envelope =
                publicDataApiClient.get(
                        PATH,
                        params,
                        new TypeReference<PublicDataEnvelope<ProductBodyResponseDto>>() {
                        }
                );

        if (envelope == null || envelope.response() == null || envelope.response().body() == null) {
            return List.of();
        }

        ProductBodyResponseDto body = envelope.response().body();
        return body.itemsOrEmpty();
    }

    public Optional<ProductItemResponseDto> findFirst(ProductSearchRequestDto req) {
        List<ProductItemResponseDto> list = search(req);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}