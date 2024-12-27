package com.example.na0th.auction.domain.bid.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BidRequest {
    private Long auctionId;
    //후에 Jwt에서 추출하는 것으로 변경될 여지 있음
    private Long userId;
    private String bidderNickName;
    private BigDecimal bidAmount;
}
