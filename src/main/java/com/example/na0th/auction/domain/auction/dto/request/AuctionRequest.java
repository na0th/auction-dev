package com.example.na0th.auction.domain.auction.dto.request;

import com.example.na0th.auction.domain.auction.model.AuctionCategory;
import com.example.na0th.auction.domain.auction.model.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private Long productId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String auctionCategory;
        private String auctionStatus;
        private BigDecimal startingBid;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private Long productId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String auctionCategory;
        private String auctionStatus;
        private BigDecimal startingBid;
    }
}
