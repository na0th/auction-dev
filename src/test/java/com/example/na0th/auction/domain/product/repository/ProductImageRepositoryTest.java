package com.example.na0th.auction.domain.product.repository;

import com.example.na0th.auction.common.config.QueryDslConfig;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.model.ProductImage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class ProductImageRepositoryTest {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Product ID들로 ProductImage들을 조회한다")
    void ProductIds_로_ProductImage_들을_조회한다() {
        // given
        Product product1 = Product.create("name1", "description1", ProductCategory.DSLR);
        Product product2 = Product.create("name2", "description2", ProductCategory.DSLR);
        productRepository.saveAll(List.of(product1, product2));

        ProductImage productImage1 = ProductImage.create("imageUrl1", product1);
        ProductImage productImage2 = ProductImage.create("imageUrl2", product1);
        ProductImage productImage3 = ProductImage.create("imageUrl3", product2);
        ProductImage productImage4 = ProductImage.create("imageUrl4", product2);
        productImageRepository.saveAll(List.of(productImage1, productImage2, productImage3, productImage4));

        List<Long> productIds = List.of(product1.getId(), product2.getId());

        // when
        List<ProductImage> productImageList = productImageRepository.findAllByProductIds(productIds);

        // then
        Assertions.assertThat(productImageList)
                .hasSize(4)
                .containsExactlyInAnyOrder(productImage1, productImage2, productImage3, productImage4);
    }

}
