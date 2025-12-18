package com.example.trytwoseongbullbe.domain.reference.product.controller;

import com.example.trytwoseongbullbe.domain.reference.product.dto.request.ProductSearchRequestDto;
import com.example.trytwoseongbullbe.domain.reference.product.dto.response.ProductItemResponseDto;
import com.example.trytwoseongbullbe.domain.reference.product.service.ProductService;
import com.example.trytwoseongbullbe.global.response.ApiResponse;
import java.util.List;
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

    /**
     * 세부품명 목록 조회
     */
    @Override
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductItemResponseDto>>> search(
            @ModelAttribute ProductSearchRequestDto req) {

        List<ProductItemResponseDto> items = productService.search(req);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
}