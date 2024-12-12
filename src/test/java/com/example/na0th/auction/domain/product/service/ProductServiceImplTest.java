package com.example.na0th.auction.domain.product.service;

import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.repository.ProductImageRepository;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import com.example.na0th.auction.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    ProductImageRepository productImageRepository;
    @InjectMocks
    ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("name")
                .description("description")
                .productCategory(ProductCategory.TV)
                .build();
    }

    @Test
    @DisplayName("설명")
    void 상품을_생성한다() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("설명")
    void 상품을_상품ID로_조회한다() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("설명")
    void 상품을_수정한다() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("설명")
    void 상품을_삭제한다() {
        // given

        // when

        // then
    }
}