package com.example.na0th.auction.domain.bid.service;

import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import com.example.na0th.auction.domain.bid.dto.request.BidRequest;
import com.example.na0th.auction.domain.bid.model.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final AuctionRepository auctionRepository;

    /**
     todo 최고 입찰 변경 시 highestBidAmount, highestBidderId를 교체해야 함
      최고 입찰이 아닌 경우 예외 처리함
     */
    @Transactional
    @Override
    public void processBid(BidRequest request) {
        Auction foundAuction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found" + request.getAuctionId()));
        Bid newBid = Bid.create(request.getBidAmount(), request.getBidderNickName());
        foundAuction.updateHighestBid(request.getUserId(),newBid);

        //변경감지 사용, 영속성 전이로 auction save 할 때, bid 도 save 된다
        //auctionRepository.save(foundAuction);
    }
}
