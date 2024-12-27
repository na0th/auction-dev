package com.example.na0th.auction.domain.product.repository.custom;

import com.example.na0th.auction.domain.product.model.ProductImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.na0th.auction.domain.product.model.QProductImage.productImage;

@Repository
@RequiredArgsConstructor
public class ProductImageRepositoryCustomImpl implements ProductImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<ProductImage> findAllByProductIds(List<Long> productIds) {
        //Product ID List 에 속하는 Product 이미지들을 한 꺼번에 batch 조회
        return queryFactory
                .selectFrom(productImage)
                .where(productImage.product.id.in(productIds))
                .fetch();
    }
}
