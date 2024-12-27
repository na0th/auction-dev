package com.example.na0th.auction.domain.product.dto.response;

import com.example.na0th.auction.domain.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long productId;
    private String productName;
    private String description;
    private String productCategory;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .productCategory(product.getProductCategory().getDisplayName())
                .build();
    }

}
