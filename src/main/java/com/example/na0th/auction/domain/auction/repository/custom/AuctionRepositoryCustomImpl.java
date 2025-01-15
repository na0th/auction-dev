package com.example.na0th.auction.domain.auction.repository.custom;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponseWon;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.AuctionCategory;
import com.example.na0th.auction.domain.auction.model.AuctionStatus;
import com.example.na0th.auction.domain.auction.model.QAuction;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.user.model.QUser;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
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


@Repository
@RequiredArgsConstructor
public class AuctionRepositoryCustomImpl implements AuctionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUser user = new QUser("user");
    QUser seller = new QUser("seller");
    QUser highestBidder = new QUser("highestBidder");


    @Override
    public Page<Auction> getAuctions(Pageable pageable, AuctionRequest.SearchCondition condition) {
        List<Auction> content = searchAuctions(condition);
        long totalCount = buildCountQuery(condition);
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<Auction> findWonAuctionsByUserId(Long userId, Pageable pageable) {
        List<Auction> content = searchWonAuctionsByUserId();
        long totalCount = queryFactory
                .select(auction.id)
                .from(auction)
                .join(highestBidder)
                .on(auction.highestBidderId.eq(highestBidder.id)
                        .and(auctionStatusEq(AuctionStatus.CLOSED))
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AuctionResponse.Won> findWonAuctionsByUserIdV3(Long userId, Pageable pageable) {
        List<AuctionResponse.Won> content = searchWonAuctionsByUserIdV4();
        long totalCount = queryFactory
                .select(auction.id)
                .from(auction)
                .join(highestBidder)
                .on(auction.highestBidderId.eq(highestBidder.id)
                        .and(auctionStatusEq(AuctionStatus.CLOSED))
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<AuctionResponseWon> findWonAuctionsByUserIdV5(Long userId, Pageable pageable) {
        List<AuctionResponseWon> content = searchWonAuctionsByUserIdV5(userId);
//        long totalCount = queryFactory
//                .select(auction.id)
//                .from(auction)
//                .join(highestBidder)
//                .on(auction.highestBidderId.eq(highestBidder.id)
//                        .and(auctionStatusEq(AuctionStatus.CLOSED))
//                )
//                .fetchCount();
        long totalCount = 5;
        return new PageImpl<>(content, pageable, totalCount);
    }

    private List<Auction> searchWonAuctionsByUserId() {

        return queryFactory
                .select(auction)
                .from(auction)
                .join(auction.product, product)
//                .fetchJoin()
                .join(auction.seller, seller)
//                .fetchJoin()
                .join(highestBidder)
                .on(
                        auction.highestBidderId.isNotNull()
                                .and(auction.highestBidderId.eq(highestBidder.id))
                                //CLOSED 는 낙찰됐다는 것, CANCEL 은 유찰됐다는 것
                                .and(auctionStatusEq(AuctionStatus.CLOSED))
                )
//                .fetchJoin()
                .fetch();
    }

    // V2는 highest bidder를 넘겨줄 수 없다.. -> select은 auction 기준이니까..
    // auction에는 highest bidder라는 엔티티와 매핑되지 않음!
    private List<Auction> searchWonAuctionsByUserIdV2() {
        return queryFactory
                .selectFrom(auction)
                .join(auction.product, product)
                .fetchJoin()
                .join(auction.seller, seller)
                .fetchJoin()
                .join(highestBidder)
                .on(auction.highestBidderId.eq(highestBidder.id)
                        //CLOSED 는 낙찰됐다는 것, CANCEL 은 유찰됐다는 것
                        .and(auctionStatusEq(AuctionStatus.CLOSED))
                )
                .fetchJoin()
                .fetch();
    }

    //엔티티 반환이 아닌, DTO 로 Response 를 반환
    private List<AuctionResponse.Won> searchWonAuctionsByUserIdV3() {
        return queryFactory
                .select(Projections.constructor(AuctionResponse.Won.class,
                        auction.id,
                        auction.auctionTime.startTime,
                        auction.auctionTime.endTime,
                        auction.auctionCategory.stringValue(),
                        auction.auctionStatus.stringValue(),
                        auction.startingBid,
                        auction.reservePrice,
                        auction.highestBidAmount,
                        highestBidder.nickName,
                        seller.id,
                        seller.nickName,
                        product.id,
                        product.productCategory.stringValue(),
                        product.description
                ))
                .join(auction.product, product)
                .fetchJoin()
                .join(auction.seller, seller)
                .fetchJoin()
                .join(highestBidder)
                .on(auction.highestBidderId.eq(highestBidder.id)
                        //CLOSED 는 낙찰됐다는 것, CANCEL 은 유찰됐다는 것
                        .and(auctionStatusEq(AuctionStatus.CLOSED))
                )
                .fetchJoin()
                .fetch();
    }

    private List<AuctionResponseWon> searchWonAuctionsByUserIdV5(Long userId) {
        JPAQuery<AuctionResponseWon> query = queryFactory
                .select(Projections.constructor(AuctionResponseWon.class,
                        auction.id,
                        auction.auctionTime.startTime,
                        auction.auctionTime.endTime,
                        auction.auctionCategory.stringValue(),
                        auction.auctionStatus.stringValue(),
                        auction.startingBid,
                        auction.reservePrice,
                        auction.highestBidAmount,
                        highestBidder.nickName,
                        seller.id,
                        seller.nickName,
                        product.id,
                        product.productCategory.stringValue(),
                        product.description
                ))
                .from(auction)
                .join(auction.product, product)
                .join(auction.seller, seller)
                .join(highestBidder)
                .on(auction.highestBidderId.eq(highestBidder.id)
                        .and(auctionStatusEq(AuctionStatus.CLOSED))
                );
        System.out.println(query.toString());

        return query.fetch();
    }

    private List<AuctionResponse.Won> searchWonAuctionsByUserIdV4() {
        return queryFactory
                .select(Projections.fields(AuctionResponse.Won.class,
                        auction.id.as("auctionId"),
                        auction.auctionTime.startTime.as("startTime"),
                        auction.auctionTime.endTime.as("endTime"),
                        ExpressionUtils.as(auction.auctionCategory.stringValue(), "auctionCategory"),
//                        auction.auctionCategory.as("auctionCategory").stringValue(),
                        ExpressionUtils.as(auction.auctionStatus.stringValue(), "auctionStatus"),
//                        auction.auctionStatus.as("auctionStatus").stringValue(),
                        auction.startingBid.as("startingBid"),
                        auction.reservePrice.as("reservePrice"),
                        auction.highestBidAmount.as("highestBidAmount"),
                        highestBidder.nickName.as("highestBidderNickname"),
                        seller.id.as("sellerId"),
                        seller.nickName.as("sellerNickName"),
                        product.id.as("productId"),
                        ExpressionUtils.as(product.productCategory.stringValue(), "productCategory"),
//                        product.productCategory.as("productCategory").stringValue(),
                        product.description.as("description")
                ))
                .from(auction)
                .join(auction.product, product)
//                .fetchJoin()
                .join(auction.seller, seller)
//                .fetchJoin()
                .join(highestBidder)
                .on(auction.highestBidderId.eq(highestBidder.id)
                        //CLOSED 는 낙찰됐다는 것, CANCEL 은 유찰됐다는 것
                        .and(auctionStatusEq(AuctionStatus.CLOSED))
                )
//                .fetchJoin()
                .fetch();
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
