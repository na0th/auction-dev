package com.example.na0th.auction.domain.auction.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuctionEndedEvent {
    private final Long auctionId;
}
