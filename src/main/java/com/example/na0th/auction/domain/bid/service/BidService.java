package com.example.na0th.auction.domain.bid.service;

import com.example.na0th.auction.domain.bid.dto.request.BidRequest;

public interface BidService {
    void processBid(BidRequest message);
}
