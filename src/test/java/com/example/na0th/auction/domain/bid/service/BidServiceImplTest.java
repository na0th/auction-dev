package com.example.na0th.auction.domain.bid.service;

import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.AuctionStatus;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import com.example.na0th.auction.domain.bid.dto.request.BidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {
    @Mock
    private AuctionRepository auctionRepository;
    @InjectMocks
    private BidServiceImpl bidService;

    private BidRequest bidRequest;

    private Auction auction;

    //트랜잭션 관련 테스트는 통합으로 진행해야 할 듯
    @BeforeEach
    void setUp() {
        bidRequest = new BidRequest(1L, 1L, "testUser", BigDecimal.valueOf(1000));
        auction = Auction.builder()
                .auctionStatus(AuctionStatus.ACTIVE)
                .startingBid(BigDecimal.ZERO)
                .highestBidderId(null)
                .highestBidAmount(null)
                .bids(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("유효한 입찰일 시 최고 입찰가, 최고 입찰자, 입찰을 업데이트합니다.")
    void 유효한_입찰_요청_처리를_성공한다() {
        // given
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        // when
        bidService.processBid(bidRequest);
        // then
        assertEquals(BigDecimal.valueOf(1000), auction.getHighestBidAmount());
        assertEquals(1L, auction.getHighestBidderId());
        assertEquals(auction.getBids().size(), 1);
    }

    @Test
    @DisplayName("경매가 존재하지 않으면 예외를 발생시킨다.")
    void 경매_없음_예외() {
        // Given
        when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AuctionNotFoundException.class, () -> bidService.processBid(bidRequest));
    }
}
