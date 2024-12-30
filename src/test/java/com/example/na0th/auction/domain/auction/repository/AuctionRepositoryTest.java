package com.example.na0th.auction.domain.auction.repository;

import com.example.na0th.auction.common.config.QueryDslConfig;
import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.AuctionCategory;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(QueryDslConfig.class)
class AuctionRepositoryTest {

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("검색 필터로 만들어진 동적 쿼리가 페이징 처리 돼 반환되면 성공한다.")
    void 검색_조건_동적_쿼리_결과가_페이징_되어_반환되면_성공한다() {
        // given
        Product product1 = Product.builder()
                .name("product1")
                .productCategory(ProductCategory.TV)
                .build();
        Product product2 = Product.builder()
                .name("product1")
                .productCategory(ProductCategory.TV)
                .build();
        productRepository.saveAll(List.of(product1, product2));
        Auction auction1 = Auction.builder()
                .auctionCategory(AuctionCategory.PUBLIC_BID)
                .product(product1)
                .reservePrice(BigDecimal.ZERO)
                .build();
        Auction auction2 = Auction.builder()
                .auctionCategory(AuctionCategory.PUBLIC_FREE)
                .product(product2)
                .reservePrice(BigDecimal.ZERO)
                .build();
        auctionRepository.saveAll(List.of(auction1, auction2));

        Pageable pageable = PageRequest.of(0, 10);
        AuctionRequest.SearchCondition condition = new AuctionRequest.SearchCondition(AuctionCategory.PUBLIC_BID, ProductCategory.TV, null, null, null, null, null, null);
        // when
        Page<Auction> auctions = auctionRepository.getAuctions(pageable, condition);
        // then
        assertAll(
                () -> assertThat(auctions).hasSize(1),
                () -> assertThat(auctions.getContent().get(0)).isEqualTo(auction1),
                () -> assertThat(auctions.getContent().get(0).getProduct().getProductCategory())
                        .isEqualTo(ProductCategory.TV)
        );

    }
}