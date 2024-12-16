package com.example.na0th.auction.domain.auction.service;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;

public interface AuctionService {

    AuctionResponse create(Long userId, AuctionRequest.Create request);

    AuctionResponse getById(Long auctionId);

    AuctionResponse update(Long auctionId, AuctionRequest.Update request);

    void delete(Long auctionId);
}
