package com.example.na0th.auction.domain.auction.service;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.AuctionCategory;
import com.example.na0th.auction.domain.auction.model.AuctionStatus;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import com.example.na0th.auction.domain.product.exception.ProductNotFoundException;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import com.example.na0th.auction.domain.user.exception.UserNotFoundException;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public AuctionResponse create(Long userId, AuctionRequest.Create request) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        Product foundProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found" + request.getProductId()));

        //auctionTime 관련 검증은 auctionTime에서 한다
        Auction newAuction = Auction.create(
                foundUser,
                foundProduct,
                request.getStartTime(),
                request.getEndTime(),
                request.getStartingBid(),
                request.getAuctionCategory(),
                request.getAuctionStatus()
        );

        auctionRepository.save(newAuction);

        return AuctionResponse.of(newAuction);

    }

    @Override
    public AuctionResponse getById(Long auctionId) {
        Auction foundAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with id " + auctionId));

        return AuctionResponse.of(foundAuction);
    }

    @Override
    public AuctionResponse update(Long auctionId, AuctionRequest.Update request) {
        /**
         * 업데이트는 PENDING 상태에서만 가능해야 한다
         * 경매 종료됨, 경매 취소됨 상태에서는 업데이트 X
         * 진행중일 때는 어떻게 할 것인지?
         *
         * 그리고 Product가 변경이 되는 경우는?
         */
        Auction foundAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with id " + auctionId));
        //내부에서 PENDING인지 검증함
        foundAuction.update(
                request.getStartTime(),
                request.getEndTime(),
                request.getAuctionCategory(),
                request.getAuctionStatus(),
                request.getStartingBid()
        );
        Auction updatedAuction = auctionRepository.save(foundAuction);

        return AuctionResponse.of(updatedAuction);
    }

    @Override
    public void delete(Long auctionId) {
        /**
         * 아직은 어떤 방식을 삭제를 구현할지 못정함
         * PENDING 때만 삭제 가능?
         * BID가 없을 때 삭제 가능?
         * 아니면 삭제 자체가 불가능?
         */
    }
}
