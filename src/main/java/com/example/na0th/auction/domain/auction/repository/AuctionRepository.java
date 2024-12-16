package com.example.na0th.auction.domain.auction.repository;

import com.example.na0th.auction.domain.auction.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
