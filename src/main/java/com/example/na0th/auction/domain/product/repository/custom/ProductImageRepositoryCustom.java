package com.example.na0th.auction.domain.product.repository.custom;

import com.example.na0th.auction.domain.product.model.ProductImage;

import java.util.List;

public interface ProductImageRepositoryCustom {
    List<ProductImage> findAllByProductIds(List<Long> productIds);
}
