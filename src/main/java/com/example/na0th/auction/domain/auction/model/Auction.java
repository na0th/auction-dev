package com.example.na0th.auction.domain.auction.model;

import com.example.na0th.auction.common.entity.BaseEntity;
import com.example.na0th.auction.domain.auction.exception.AuctionCanNotUpdateException;
import com.example.na0th.auction.domain.auction.exception.InvalidAuctionTimeException;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private AuctionTime auctionTime;

    @Enumerated(EnumType.STRING)
    private AuctionCategory auctionCategory;

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @Column(nullable = true)
    private BigDecimal startingBid;

    // Auction - user 다대일 -> 다 쪽에 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    @OneToOne
    private Product product;
//    private List<Bid> bids = new ArrayList<>();


    public static Auction create(User seller, Product product, LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingBid, String auctionCategory, String auctionStatus) {
        AuctionTime auctionTime = AuctionTime.of(startTime, endTime);

        Auction newAuction = Auction.builder()
                .seller(seller)
                .product(product)
                .auctionTime(auctionTime)
                .auctionCategory(AuctionCategory.fromDisplayName(auctionCategory))
                .auctionStatus(AuctionStatus.fromDisplayName(auctionStatus))
                .startingBid(startingBid)
                .build();

        newAuction.addUser(seller);
        return newAuction;
    }

    //일단은 단방향 auction이 user의 참조만 들고 있다. user가 auction을 참조하는 건 이상함(user가 여러 개의 옥션에 참여가능)
    public void addUser(User user) {
        this.seller = user;
    }

    public void update(LocalDateTime startTime, LocalDateTime endTime, String auctionCategory, String auctionStatus, BigDecimal startingBid) {
        ensureCanUpdate();
        this.auctionTime = AuctionTime.of(startTime, endTime);
        this.auctionCategory = AuctionCategory.fromDisplayName(auctionCategory);
        this.auctionStatus = AuctionStatus.fromDisplayName(auctionStatus);
        this.startingBid = startingBid;
    }

    private boolean ensureCanUpdate() {
        if (!auctionStatus.equals(AuctionStatus.PENDING)) {
            throw new AuctionCanNotUpdateException("Auction Can Not Update : status is not PENDING");
        }
        return true;
    }
}
