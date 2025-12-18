package com.example.trytwoseongbullbe.domain.reference.business.implement;

import com.example.trytwoseongbullbe.domain.reference.business.dto.request.BusinessSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.business.dto.response.BusinessBodyResponseDto;
import com.example.trytwoseongbullbe.domain.reference.business.dto.response.BusinessItemResponseDto;
import com.example.trytwoseongbullbe.infrastructure.publicdata.PublicDataApiClient;
import com.example.trytwoseongbullbe.infrastructure.publicdata.PublicDataEnvelope;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class BusinessApiClient {

    private static final String PATH =
            "/1230000/ao/IndstrytyBaseLawrgltInfoService/getIndstrytyBaseLawrgltInfoList";

    private final PublicDataApiClient publicDataApiClient;

    public BusinessApiClient(PublicDataApiClient publicDataApiClient) {
        this.publicDataApiClient = publicDataApiClient;
    }

    public List<BusinessItemResponseDto> search(BusinessSearchRequestDto req) {
        Map<String, Object> params = new HashMap<>();
        params.put("indstrytyCd", req.indstrytyCd());
        params.put("pageNo", req.pageNoOrDefault());
        params.put("numOfRows", req.numOfRowsOrDefault());

        PublicDataEnvelope<BusinessBodyResponseDto> envelope =
                publicDataApiClient.get(
                        PATH,
                        params,
                        new TypeReference<PublicDataEnvelope<BusinessBodyResponseDto>>() {
                        }
                );

        if (envelope == null || envelope.response() == null || envelope.response().body() == null) {
            return List.of();
        }

        BusinessBodyResponseDto body = envelope.response().body();
        return body.itemsOrEmpty();
    }

    public Optional<BusinessItemResponseDto> findFirst(BusinessSearchRequestDto req) {
        List<BusinessItemResponseDto> list = search(req);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}