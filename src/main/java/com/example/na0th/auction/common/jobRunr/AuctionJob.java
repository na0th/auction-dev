package com.example.na0th.auction.common.jobRunr;

import com.example.na0th.auction.domain.auction.event.AuctionEndedEvent;
import com.example.na0th.auction.domain.auction.event.AuctionStartedEvent;
import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuctionJob {

    private final AuctionRepository auctionRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 경매 시작 상태 변경

    public void startAuction(Long auctionId) {
        System.out.println("Auction 시작 이벤트 발행 전 - ID: " + auctionId);
        eventPublisher.publishEvent(new AuctionStartedEvent(auctionId));
        System.out.println("Auction 시작 이벤트 발행 후 - ID: " + auctionId);
//        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Not Found Auction ID: " + auctionId));
//        auction.activate();
//        System.out.println("경매 시작 - ID: " + auctionId);
    }

    // 경매 종료 상태 변경
    public void endAuction(Long auctionId) {
        System.out.println("Auction 종료 이벤트 발행 전 - ID: " + auctionId);
        eventPublisher.publishEvent(new AuctionEndedEvent(auctionId));
        System.out.println("Auction 종료 이벤트 발행 후 - ID: " + auctionId);
//        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Not Found Auction ID: " + auctionId));
//        auction.close();
//        System.out.println("경매 종료 - ID: " + auctionId);
    }
}
