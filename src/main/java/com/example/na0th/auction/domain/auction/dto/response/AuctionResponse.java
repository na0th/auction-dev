package com.example.na0th.auction.domain.auction.dto.response;

import com.example.na0th.auction.domain.auction.model.Auction;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionResponse {
    private Long auctionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String auctionCategory;
    private String auctionStatus;
    private BigDecimal startingBid;
    private BigDecimal reservePrice;
    private Long sellerId;
    private Long productId;

    //지연로딩으로 non query
    public static AuctionResponse of(Auction auction) {
        return AuctionResponse.builder()
                .auctionId(auction.getId())
                .startTime(auction.getAuctionTime().getStartTime())
                .endTime(auction.getAuctionTime().getEndTime())
                .auctionCategory(auction.getAuctionCategory().toString())
                .auctionStatus(auction.getAuctionStatus().toString())
                .startingBid(auction.getStartingBid())
                .reservePrice(auction.getReservePrice())
                .sellerId(auction.getSeller().getId())
                .productId(auction.getProduct().getId())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    //지연로딩해도 쿼리 나감(N+1 문제 해결 해야 함 -> Fetch Join  || Batch Size 설정)
    public static class Details {
        //auction
        private Long auctionId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String auctionCategory;
        private String auctionStatus;
        private BigDecimal startingBid;
        private BigDecimal reservePrice;
        //user
        private Long sellerId;
        private String sellerNickName;
        //product
        private Long productId;
        private String productCategory;
        private String description;
        //productImage
        private List<String> productImageUrls;

        public static AuctionResponse.Details of(Auction auction, List<String> imageUrls) {
            //nullable 필드들은 null 체크가 필요하다.
            return Details.builder()
                    .auctionId(auction.getId())
                    .startTime(auction.getAuctionTime().getStartTime())
                    .endTime(auction.getAuctionTime().getEndTime())
                    .auctionCategory(auction.getAuctionCategory().name())
                    .auctionStatus(auction.getAuctionStatus().name())
                    .startingBid(auction.getStartingBid())
                    .reservePrice(auction.getReservePrice())
                    .sellerId(auction.getSeller().getId())
                    .sellerNickName(auction.getSeller().getNickName())
                    .productId(auction.getProduct().getId())
                    .productCategory(auction.getProduct().getProductCategory().name())
                    .description(auction.getProduct().getDescription())
                    .productImageUrls(imageUrls != null ? imageUrls : Collections.emptyList())
                    .build();
        }

    }

    //낙찰받은 경매 Response
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class Won {
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

        public AuctionResponse.Won addProductImageUrls(List<String> imageUrls) {
            this.productImageUrls = imageUrls;
            return this;
        }

        public Won(Long auctionId,
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
                   String description) {
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
//        public static AuctionResponse.Won of(Auction auction,String highestBidder, List<String> imageUrls) {
//            return Won.builder()
//                    .auctionId(auction.getId())
//                    .startTime(auction.getAuctionTime().getStartTime())
//                    .endTime(auction.getAuctionTime().getEndTime())
//                    .auctionCategory(auction.getAuctionCategory().name())
//                    .auctionStatus(auction.getAuctionStatus().name())
//                    .startingBid(auction.getStartingBid())
//                    .reservePrice(auction.getReservePrice())
//                    .highestBidAmount(auction.getHighestBidAmount())
//
//                    .highestBidderNickname(auction.get)
//
//                    .sellerId(auction.getSeller().getId())
//                    .sellerNickName(auction.getSeller().getNickName())
//
//                    .productId(auction.getProduct().getId())
//                    .productCategory(auction.getProduct().getProductCategory())
//                    .description(auction.getProduct().getDescription())
//
//                    .productImageUrls(imageUrls)
//
//                    .build();
//        }
    }


}
