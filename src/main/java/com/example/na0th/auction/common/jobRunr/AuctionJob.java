package com.example.na0th.auction.common.jobRunr;

import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuctionJob {

    private final AuctionRepository auctionRepository;

    // 경매 시작 상태 변경
    @Transactional
    public void startAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Not Found Auction ID: " + auctionId));
        auction.activate();
        System.out.println("경매 시작 - ID: " + auctionId);
    }

    // 경매 종료 상태 변경
    @Transactional
    public void endAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Not Found Auction ID: " + auctionId));
        if (auction.isCanceled()) {
            auction.cancel();
        } else {
            auction.close();
        }
        /**
         * 만약, 최고 입찰가가 지정가보다 낮다면 유찰된다.
         * 유찰 -> CANCELD
         * 낙찰 -> CLOSED
         */
        System.out.println("경매 종료 - ID: " + auctionId);
    }
}
