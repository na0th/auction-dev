package com.example.na0th.auction.domain.product.repository;

import com.example.na0th.auction.domain.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
