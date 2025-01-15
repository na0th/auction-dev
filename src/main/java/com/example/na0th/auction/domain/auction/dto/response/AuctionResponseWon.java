package com.example.na0th.auction.domain.auction.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class AuctionResponseWon {
    //auction
    private Long auctionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String auctionCategory;
    private String auctionStatus;
    private BigDecimal startingBid;
    private BigDecimal reservePrice;
    private BigDecimal highestBidAmount;

    //Bidder
    private String highestBidderNickname;

    //user
    private Long sellerId;
    private String sellerNickName;
    //product
    private Long productId;
    private String productCategory;
    private String description;
    //productImage
    private List<String> productImageUrls;

    public AuctionResponseWon addProductImageUrls(List<String> imageUrls) {
        this.productImageUrls = imageUrls;
        return this;
    }

    public AuctionResponseWon(Long auctionId,
               LocalDateTime startTime,
               LocalDateTime endTime,
               String auctionCategory,
               String auctionStatus,
               BigDecimal startingBid,
               BigDecimal reservePrice,
               BigDecimal highestBidAmount,
               String highestBidderNickname,
               Long sellerId,
               String sellerNickName,
               Long productId,
               String productCategory,
               String description
    ) {
        this.auctionId = auctionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.auctionCategory = auctionCategory;
        this.auctionStatus = auctionStatus;
        this.startingBid = startingBid;
        this.reservePrice = reservePrice;
        this.highestBidAmount = highestBidAmount;

        this.highestBidderNickname = highestBidderNickname;

        this.sellerId = sellerId;
        this.sellerNickName = sellerNickName;

        this.productId = productId;
            this.productCategory = productCategory;
            this.description = description;
    }
}
