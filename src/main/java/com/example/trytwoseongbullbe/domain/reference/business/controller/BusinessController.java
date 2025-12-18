package com.example.trytwoseongbullbe.domain.reference.business.controller;

import com.example.trytwoseongbullbe.domain.reference.business.dto.request.BusinessSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.business.dto.response.BusinessSimpleResponseDto;
import com.example.trytwoseongbullbe.domain.reference.business.service.BusinessService;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/reference")
public class BusinessController implements BusinessControllerDocs {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @Override
    public ResponseEntity<ApiResponse<BusinessSimpleResponseDto>> search(
            @ModelAttribute BusinessSearchRequestDto req
    ) {
        Optional<BusinessSimpleResponseDto> result = businessService.searchOne(req);

        if (result.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(result.get()));
        }

        return ResponseEntity.ok(ApiResponse.error(ErrorType.PRODUCT_NOT_FOUND));
    }
}