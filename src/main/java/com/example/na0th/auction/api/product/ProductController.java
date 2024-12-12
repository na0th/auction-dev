package com.example.na0th.auction.api.product;

import com.example.na0th.auction.common.response.ApiResult;
import com.example.na0th.auction.domain.product.dto.request.ProductRequest;
import com.example.na0th.auction.domain.product.dto.response.ProductResponse;
import com.example.na0th.auction.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResult<ProductResponse>> create(@RequestPart("productRequest") ProductRequest.Create request, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        ProductResponse createdProduct = productService.create(request, images);
        return ResponseEntity.ok(ApiResult.success(PRODUCT_CREATED_SUCCESS, createdProduct));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResult<ProductResponse>> get(@PathVariable Long productId) {
        ProductResponse foundProduct = productService.getById(productId);
        return ResponseEntity.ok(ApiResult.success(null, foundProduct));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResult<ProductResponse>> update(@PathVariable Long productId, @RequestBody ProductRequest.Update request) {
        ProductResponse updatedProduct = productService.update(productId, request);
        return ResponseEntity.ok(ApiResult.success(PRODUCT_UPDATED_SUCCESS, updatedProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResult<ProductResponse>> delete(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok(ApiResult.success(PRODUCT_DELETED_SUCCESS));
    }
}
