package com.example.na0th.auction.domain.auction.repository.custom;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.AuctionCategory;
import com.example.na0th.auction.domain.auction.model.AuctionStatus;
import com.example.na0th.auction.domain.auction.model.QAuction;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.model.ProductImage;
import com.example.na0th.auction.domain.product.model.QProductImage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.na0th.auction.domain.auction.model.QAuction.auction;
import static com.example.na0th.auction.domain.product.model.QProduct.product;
import static com.example.na0th.auction.domain.product.model.QProductImage.productImage;
import static com.example.na0th.auction.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryCustomImpl implements AuctionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Auction> getAuctions(Pageable pageable, AuctionRequest.SearchCondition condition) {
        List<Auction> content = searchAuctions(condition);
        long totalCount = buildCountQuery(condition);
        return new PageImpl<>(content, pageable, totalCount);
    }

    private List<Auction> searchAuctions(AuctionRequest.SearchCondition condition) {
        return queryFactory
                .selectFrom(auction)
                .leftJoin(auction.product, product)
                .fetchJoin()
                .leftJoin(auction.seller, user)
                .fetchJoin()
                .where(
                        auctionCategoryEq(condition.getAuctionCategory()),
                        productCategoryEq(condition.getProductCategory()),
                        startTimeAfter(condition.getStartTime()),
                        endTimeBefore(condition.getEndTime()),
                        highestBidGoe(condition.getMinPrice()),
                        highestBidLoe(condition.getMaxPrice()),
                        auctionStatusEq(condition.getAuctionStatus())
                )
                .orderBy(sortBy(condition.getSortBy()))
                .fetch();

    }

    private long buildCountQuery(AuctionRequest.SearchCondition condition) {
        return queryFactory
                .selectFrom(auction)
                .where(
                        auctionCategoryEq(condition.getAuctionCategory()),
                        productCategoryEq(condition.getProductCategory()),
                        startTimeAfter(condition.getStartTime()),
                        endTimeBefore(condition.getEndTime()),
                        highestBidGoe(condition.getMinPrice()),
                        highestBidLoe(condition.getMaxPrice()),
                        auctionStatusEq(condition.getAuctionStatus())
                )
                .fetchCount();
    }


    private Predicate auctionCategoryEq(AuctionCategory auctionCategory) {
        return auctionCategory != null ? auction.auctionCategory.eq(auctionCategory) : null;
    }

    private Predicate productCategoryEq(ProductCategory productCategory) {
        return productCategory != null ? product.productCategory.eq(productCategory) : null;
    }

    private Predicate startTimeAfter(LocalDateTime startTime) {
        return startTime != null ? auction.auctionTime.startTime.after(startTime) : null;
    }

    private Predicate endTimeBefore(LocalDateTime endTime) {
        return endTime != null ? auction.auctionTime.endTime.before(endTime) : null;
    }

    private Predicate auctionStatusEq(AuctionStatus auctionStatus) {
        return auctionStatus != null ? auction.auctionStatus.eq(auctionStatus) : null;
    }

    private Predicate highestBidGoe(BigDecimal minPrice) {
        return minPrice != null ? auction.highestBidAmount.goe(minPrice) : null;
    }

    private Predicate highestBidLoe(BigDecimal maxPrice) {
        return maxPrice != null ? auction.highestBidAmount.loe(maxPrice) : null;
    }

    private OrderSpecifier<?> sortBy(String sortBy) {
        if (sortBy == null) {
            //기본 정렬은 최신 등록 순(startTime 기준이 아님)
            return auction.createdAt.desc();
        }
        return switch (sortBy) {
            //최고 입찰 높은 순
            case "highestBid" -> auction.highestBidAmount.desc();
            //경매 시작 임박 순
            case "startSoon" -> auction.auctionTime.startTime.asc();
            //마감 임박 순
            case "endSoon" -> auction.auctionTime.endTime.asc();
            default -> auction.createdAt.desc();
        };
    }
}
