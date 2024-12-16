package com.example.na0th.auction.domain.auction.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AuctionStatus {
    PENDING("보류중"),
    ACTIVE("경매 진행중"),
    CLOSED("경매 종료됨"),
    CANCELED("경매 취소됨");

    private final String displayName;

    AuctionStatus(String displayName) {
        this.displayName = displayName;
    }

    public static AuctionStatus fromDisplayName(String auctionStatus) {
        for (AuctionStatus status : values()) {
            if (status.name().equalsIgnoreCase(auctionStatus)) {
                return status;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 경매 상태 : " + auctionStatus);
    }
}
