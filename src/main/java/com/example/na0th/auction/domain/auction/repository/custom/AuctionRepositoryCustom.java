package com.example.na0th.auction.domain.auction.repository.custom;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.model.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositoryCustom {
    Page<Auction> getAuctions(Pageable pageable, AuctionRequest.SearchCondition condition);
}
