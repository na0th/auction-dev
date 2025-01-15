package com.example.na0th.auction.domain.auction.repository.custom;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponseWon;
import com.example.na0th.auction.domain.auction.model.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositoryCustom {
    Page<Auction> getAuctions(Pageable pageable, AuctionRequest.SearchCondition condition);
    Page<Auction> findWonAuctionsByUserId(Long userId, Pageable pageable);
    Page<AuctionResponse.Won> findWonAuctionsByUserIdV3(Long userId, Pageable pageable);
    Page<AuctionResponseWon> findWonAuctionsByUserIdV5(Long userId, Pageable pageable);
}
