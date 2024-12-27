package com.example.na0th.auction.domain.auction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AuctionCategory {
    PUBLIC_FREE("공개 나눔"),
    PUBLIC_BID("공개 입찰"),
    PRIVATE_BID("비밀 입찰");

    private final String displayName;

    AuctionCategory(String displayName) {
        this.displayName = displayName;
    }

    public static AuctionCategory fromDisplayName(String auctionCategory) {
        for (AuctionCategory category : values()) {
            if (category.name().equalsIgnoreCase(auctionCategory)) {
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 경매 카테고리 : " + auctionCategory);
    }

}
