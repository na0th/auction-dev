package com.example.na0th.auction.domain.bid.model;

import com.example.na0th.auction.domain.auction.model.Auction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 일단 IDENTITY -> SEQUENCE, TABLE로 변경 고려
    private Long id;

    private BigDecimal bidAmount;

    //입찰자 식별자
    private String bidderNickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    public static Bid create(BigDecimal bidAmount, String bidderNickName) {
        return Bid.builder()
                .bidAmount(bidAmount)
                .bidderNickName(bidderNickName)
                .build();
    }

    public void addAuction(Auction auction) {
        this.auction = auction;
    }
}
