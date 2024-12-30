package com.example.na0th.auction.domain.auction.service;

import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.exception.AuctionCanNotUpdateException;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.AuctionCategory;
import com.example.na0th.auction.domain.auction.model.AuctionStatus;
import com.example.na0th.auction.domain.auction.model.AuctionTime;
import com.example.na0th.auction.domain.auction.repository.AuctionRepository;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.model.ProductImage;
import com.example.na0th.auction.domain.product.repository.ProductImageRepository;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import com.example.na0th.auction.domain.user.model.User;
import com.example.na0th.auction.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceImplTest {
    @InjectMocks
    private AuctionServiceImpl auctionService;
    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductImageRepository productImageRepository;
    @Mock
    private AuctionSchedulerService auctionSchedulerService;

    private User user;
    private Product product;
    private Auction auction;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("testUser")
                .nickName("testNick")
                .password("testPassword")
                .email("test@example.com")
                .build();

        product = Product.builder()
                .id(1L)
                .name("testProduct")
                .description("testDescription")
                .productCategory(ProductCategory.TV)
                .build();

        auction = Auction.builder()
                .id(1L)
                .auctionTime(AuctionTime.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1)))
                .auctionCategory(AuctionCategory.PUBLIC_BID)
                .auctionStatus(AuctionStatus.PENDING)
                .startingBid(BigDecimal.ZERO)
                .highestBidderId(null)
                .highestBidAmount(null)
                .seller(user)
                .product(product)
                .build();
    }

    @Test
    @DisplayName("경매를 생성한다")
    void 경매_생성() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        AuctionRequest.Create request = new AuctionRequest.Create(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "PUBLIC_BID", "ACTIVE", BigDecimal.ZERO, BigDecimal.ZERO);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);

        // when
        AuctionResponse createdAuction = auctionService.create(userId, request);
        // then
        assertThat(createdAuction).isNotNull();
        assertThat(createdAuction.getAuctionCategory()).isEqualTo(AuctionCategory.PUBLIC_BID.toString());
        assertThat(createdAuction.getAuctionStatus()).isEqualTo(AuctionStatus.ACTIVE.toString());

        verify(auctionSchedulerService, times(1)).scheduleStart(anyLong(), any(LocalDateTime.class));
        verify(auctionSchedulerService, times(1)).scheduleEnd(anyLong(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("경매조회")
    void 경매_조회() {
        // given
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        // when
        AuctionResponse foundAuction = auctionService.getById(1L);
        // then
        assertThat(foundAuction).isNotNull();
    }

    @Test
    @DisplayName("경매 상태가 PENDING 상태면 경매 수정을 성공한다")
    void 경매_수정을_성공한다() {
        // given
        AuctionRequest.Update request = new AuctionRequest.Update(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "PUBLIC_BID", "PENDING", BigDecimal.valueOf(1000));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);
        // when
        AuctionResponse updatedAuction = auctionService.update(1L, request);
        // then
        assertThat(updatedAuction).isNotNull();
        assertThat(updatedAuction.getAuctionCategory()).isEqualTo(AuctionCategory.PUBLIC_BID.toString());
        assertThat(updatedAuction.getAuctionStatus()).isEqualTo(AuctionStatus.PENDING.toString());
        assertThat(updatedAuction.getStartingBid()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("경매 상태가 PENDING 상태가 아니면 경매 수정을 실패한다")
    void 경매_수정을_실패한다() {
        // given
        Auction activeAuction = Auction.builder()
                .id(1L)
                .auctionStatus(AuctionStatus.ACTIVE)
                .build();

        AuctionRequest.Update request = new AuctionRequest.Update(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "PUBLIC_BID", "PENDING", BigDecimal.valueOf(1000));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(activeAuction));
        // when & then
        assertThatThrownBy(() -> auctionService.update(1L, request))
                .isInstanceOf(AuctionCanNotUpdateException.class);
    }

    @Test
    @DisplayName("필터조건_경매목록조회")
    void 필터조건으로_경매목록을_조회하고_이미지를_매핑한다() {
        // given
        AuctionRequest.SearchCondition condition = new AuctionRequest.SearchCondition();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Auction> auctionPage = new PageImpl<>(List.of(auction), pageable, 1);

        ProductImage image1 = ProductImage.builder()
                .id(1L)
                .imageUrl("imageUrl1")
                .product(product)
                .build();
        ProductImage image2 = ProductImage.builder()
                .id(2L)
                .imageUrl("imageUrl2")
                .product(product)
                .build();
        when(auctionRepository.getAuctions(pageable, condition)).thenReturn(auctionPage);
        when(productImageRepository.findAllByProductIds(List.of(1L))).thenReturn(List.of(image1, image2));
        // when
        Page<AuctionResponse.Details> result = auctionService.getAuctionsByFilter(pageable, condition);
        // then
        assertThat(result.getContent()).hasSize(1);

        AuctionResponse.Details details = result.getContent().get(0);
        assertThat(details.getProductImageUrls()).containsExactly("imageUrl1", "imageUrl2");
    }
}