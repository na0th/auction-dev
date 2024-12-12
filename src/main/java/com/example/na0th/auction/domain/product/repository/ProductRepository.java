package com.example.na0th.auction.domain.product.repository;

import com.example.na0th.auction.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
