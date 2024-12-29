package com.example.na0th.auction.domain.auction.model;

import com.example.na0th.auction.common.entity.BaseEntity;
import com.example.na0th.auction.domain.auction.exception.AuctionCanNotUpdateException;
import com.example.na0th.auction.domain.auction.exception.AuctionEndedException;
import com.example.na0th.auction.domain.auction.exception.InvalidAuctionStateException;
import com.example.na0th.auction.domain.bid.exception.InvalidBidAmountException;
import com.example.na0th.auction.domain.bid.exception.NotHighestBidException;
import com.example.na0th.auction.domain.bid.model.Bid;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version //낙관적 락
    private long version;

    @Embedded
    private AuctionTime auctionTime;

    @Enumerated(EnumType.STRING)
    private AuctionCategory auctionCategory;

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @Column(nullable = true)
    private BigDecimal startingBid;

    private Long highestBidderId;
    private BigDecimal highestBidAmount;
    // Auction - user 다대일 -> 다 쪽에 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();


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

    public void addBid(Bid bid) {
        this.bids.add(bid);
        bid.addAuction(this);
    }

    public void update(LocalDateTime startTime, LocalDateTime endTime, String auctionCategory, String auctionStatus, BigDecimal startingBid) {
        validateCanUpdate();
        this.auctionTime = AuctionTime.of(startTime, endTime);
        this.auctionCategory = AuctionCategory.fromDisplayName(auctionCategory);
        this.auctionStatus = AuctionStatus.fromDisplayName(auctionStatus);
        this.startingBid = startingBid;
    }

    public void updateHighestBid(Long highestBidderId, Bid bid) {
        validateBid(bid);
        this.highestBidAmount = bid.getBidAmount();
        this.highestBidderId = highestBidderId;
        //bid와 양방향-연관 관계 편의 메서드(연관 관계 주인쪽에서의 외래 키 수정만 먹히는 걸 명심!)
        this.addBid(bid);
    }

    public void validateBid(Bid bid) {
        if (!isAuctionActive()) {
            throw new AuctionEndedException("Auction Can Not Bid : Auction status is not ACTIVE");
        }
        if (!isHighestBid(bid)) {
            throw new NotHighestBidException("Bid Can Not Update : this bid is not Highest bid.");
        }
        if (!isBidHigherThanStartingBid(bid)) {
            throw new InvalidBidAmountException("Invalid Bid : bid amount is smaller than starting bid");
        }
    }

    public void activate() {
        validateCanActivate();
        this.auctionStatus = AuctionStatus.ACTIVE;
    }

    public void close() {
        validateCanClose();
        this.auctionStatus = AuctionStatus.CLOSED;
    }

    private void validateCanClose() {
        if (!auctionStatus.equals(AuctionStatus.ACTIVE)) {
            throw new InvalidAuctionStateException("Auction state change error: Can only close an auction from ACTIVE state.");
        }
    }

    private void validateCanActivate() {
        if (!auctionStatus.equals(AuctionStatus.PENDING)) {
            throw new InvalidAuctionStateException("Auction state change error: Can only start an auction from PENDING state.");
        }
    }

    private void validateCanUpdate() {
        if (!auctionStatus.equals(AuctionStatus.PENDING)) {
            throw new AuctionCanNotUpdateException("Auction Can Not Update : status is not PENDING");
        }
    }

    private boolean isAuctionActive() {
        return auctionStatus.equals(AuctionStatus.ACTIVE);
    }

    private boolean isHighestBid(Bid bid) {
        return highestBidAmount == null || highestBidAmount.compareTo(bid.getBidAmount()) < 0;
    }

    private boolean isBidHigherThanStartingBid(Bid bid) {
        return startingBid.compareTo(bid.getBidAmount()) < 0;
    }
}
