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
import com.example.na0th.auction.domain.product.model.ProductImage;
import com.example.na0th.auction.domain.product.repository.ProductImageRepository;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import com.example.na0th.auction.domain.user.exception.UserNotFoundException;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final AuctionSchedulerService schedulerService;

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

        Auction savedAuction = auctionRepository.save(newAuction);

        // 시작 및 종료 시간 스케줄 등록
        schedulerService.scheduleStart(savedAuction.getId(), savedAuction.getAuctionTime().getStartTime());
        schedulerService.scheduleEnd(savedAuction.getId(), savedAuction.getAuctionTime().getEndTime());

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

    @Override
    public Page<AuctionResponse.Details> getAuctionsByFilter(Pageable pageable, AuctionRequest.SearchCondition condition) {
        /**
         TODO : ProductImage -> Product만 참조 가능한 단방향 매핑임.
         따라서 Product를 조회해도 ProductImage를 참조할 수 없기 때문에
         쿼리를 따로 날려 ProductImage만 조회해 온 후 DTO에 추가해야 함.
         1. Auction 조회 / 2. ProductImage 조회/ 3. DTO에 Auction, ProductImage 채우기
         */

        // 1. Fetch Join으로 연관 엔티티까지 프록시 초기화 시켜서 조회
        Page<Auction> auctions = auctionRepository.getAuctions(pageable, condition);
        // 2. Product ID 전부 가져옴
        List<Long> productIds = auctions.stream()
                .map(auction -> auction.getProduct().getId())
                .distinct()
                .toList();
        // 3. Product ID로 Product Image 를 batch 조회
        List<ProductImage> productImages = productImageRepository.findAllByProductIds(productIds);

        // 4. ProductImage 들을 각각 Product ID로 그룹화
        Map<Long, List<String>> productImageMap = productImages.stream()
                .collect(Collectors.groupingBy(
                        productImage -> productImage.getProduct().getId(),
                        Collectors.mapping(ProductImage::getImageUrl, Collectors.toList())
                ));
        // 5. ProductImageUrls 를 각 Auction DTO 에 넣어줌
        return auctions.map(
                auction ->
                        AuctionResponse.Details.of(auction, productImageMap.getOrDefault(auction.getProduct().getId(), Collections.emptyList()))
        );
    }
}
