package com.example.trytwoseongbullbe.domain.reference.product.controller;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductSimpleResponseDto;
import com.example.trytwoseongbullbe.domain.reference.product.service.ProductService;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import com.example.trytwoseongbullbe.global.response.type.ErrorType;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/reference")
public class ProductController implements ProductControllerDocs {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<ProductSimpleResponseDto>> search(
            @ModelAttribute ProductSearchRequestDto req
    ) {
        Optional<ProductSimpleResponseDto> data = productService.searchOne(req);

        if (data.isEmpty()) {
            return ResponseEntity
                    .status(ErrorType.PRODUCT_NOT_FOUND.getStatus())
                    .body(ApiResponse.error(ErrorType.PRODUCT_NOT_FOUND));
        }

        return ResponseEntity.ok(ApiResponse.success(data.get()));
    }
}