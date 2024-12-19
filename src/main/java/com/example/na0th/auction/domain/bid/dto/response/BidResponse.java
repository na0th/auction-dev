package com.example.na0th.auction.domain.bid.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidResponse {
    private Long auctionId;
    private BigDecimal bidAmount;
    private String bidderNickName;

    public BidResponse of(Long auctionId, BigDecimal bidAmount, String bidderNickName) {
        return BidResponse.builder()
                .auctionId(auctionId)
                .bidAmount(bidAmount)
                .bidderNickName(bidderNickName)
                .build();
    }
}
