package com.example.na0th.auction.domain.auction.dto.response;

import com.example.na0th.auction.domain.auction.model.Auction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        //user
        private Long sellerId;
        private String sellerNickName;
        //product
        private Long productId;
        private String productCategory;
        private String description;
        private List<String> productImageUrls;

        public static AuctionResponse.Details of(Auction auction) {
            return AuctionResponse.Details.builder()
                    .build();
        }

    }


}
