package com.example.na0th.auction.domain.product.service;

import com.example.na0th.auction.domain.product.dto.request.ProductRequest;
import com.example.na0th.auction.domain.product.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest.Create request, List<MultipartFile> images);

    ProductResponse getById(Long productId);

    ProductResponse update(Long productId, ProductRequest.Update request);

    void delete(Long productId);

}
