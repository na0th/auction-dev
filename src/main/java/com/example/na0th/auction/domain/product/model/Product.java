package com.example.na0th.auction.domain.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

//    private Auction auction;

//    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "product_id")//자식 테이블에 외래 키
//    private List<ProductImage> productImages = new ArrayList<>();

    public static Product create(String name, String description, ProductCategory productCategory) {
        return Product.builder()
                .name(name)
                .description(description)
                .productCategory(productCategory)
                .build();
    }

    public void update(String name, String description, String productCategory) {
        this.name = name;
        this.description = description;
        this.productCategory = ProductCategory.findByDisplayName(productCategory);
    }
}
