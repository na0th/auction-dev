package com.example.na0th.auction.domain.auction.event.eventListener;

import com.example.na0th.auction.domain.auction.event.AuctionEndedEvent;
import com.example.na0th.auction.domain.auction.event.AuctionStartedEvent;
import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final AuctionRepository auctionRepository;

    @EventListener
    @Transactional
    public void handleAuctionStartedEvent(AuctionStartedEvent event) {
        Auction auction = auctionRepository.findById(event.getAuctionId()).orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + event.getAuctionId()));
        auction.activate(); // 경매 상태 변경
        System.out.println("경매 시작 처리 완료 - ID: " + event.getAuctionId());
    }

    @EventListener
    @Transactional
    public void handleAuctionEndedEvent(AuctionEndedEvent event) {
        Auction auction = auctionRepository.findById(event.getAuctionId()).orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + event.getAuctionId()));
        auction.close(); // 경매 상태 변경
        System.out.println("경매 종료 처리 완료 - ID: " + event.getAuctionId());
    }
}