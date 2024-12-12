package com.example.na0th.auction.domain.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id @GeneratedValue
    private Long id;

    private String imageUrl;

    public static ProductImage create(String imageUrl) {
        return ProductImage.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
