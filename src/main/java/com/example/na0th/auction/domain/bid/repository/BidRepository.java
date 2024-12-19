package com.example.na0th.auction.domain.bid.repository;

import com.example.na0th.auction.domain.bid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
