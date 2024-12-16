package com.example.na0th.auction.domain.auction.repository.custom;

import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.QAuction;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.na0th.auction.domain.auction.model.QAuction.auction;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryCustomImpl implements AuctionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Auction> getAuction() {
        return queryFactory
                .selectFrom(auction)
                .fetch();
    }

}
