package com.example.na0th.auction.api.bid;

import com.example.na0th.auction.domain.bid.dto.request.BidRequest;
import com.example.na0th.auction.domain.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    // 클라이언트가 "/app/bid"로 메시지를 보내면 실행됨
    @MessageMapping("/bid/{auctionId}")
    @SendTo("/topic/auctions/{auctionId}")
    public BidRequest handleBid(@DestinationVariable String auctionId, BidRequest bidMessage) {
        bidService.processBid(bidMessage);
        return bidMessage; // 모든 구독자에게 입찰 메시지 전송(request와 response가 정확히 같음)
    }
}
