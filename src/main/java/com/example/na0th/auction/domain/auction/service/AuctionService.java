package com.example.na0th.auction.domain.auction.service;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuctionService {

    AuctionResponse create(Long userId, AuctionRequest.Create request);

    AuctionResponse getById(Long auctionId);

    AuctionResponse update(Long auctionId, AuctionRequest.Update request);

    void delete(Long auctionId);

    Page<AuctionResponse.Details> getAuctionsByFilter(Pageable pageable, AuctionRequest.SearchCondition condition);
}
