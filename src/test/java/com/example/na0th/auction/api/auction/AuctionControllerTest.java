package com.example.na0th.auction.api.auction;

import com.example.na0th.auction.common.config.JpaAuditingConfig;
import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.service.AuctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuctionController.class, excludeAutoConfiguration = JpaAuditingConfig.class)
class AuctionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuctionService auctionService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("경매 생성")
    void hello() throws Exception {
        // given
        AuctionRequest.Create request = new AuctionRequest.Create();
        AuctionResponse createdAuction = new AuctionResponse();
        when(auctionService.create(any(Long.class), any(AuctionRequest.Create.class))).thenReturn(createdAuction);
        // when & then
        mockMvc.perform(post("/api/v1/auctions")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(AUCTION_CREATED_SUCCESS));
    }

    @Test
    @DisplayName("특정 경매 조회")
    void hello1() throws Exception {
        // given
        AuctionResponse foundAuction = new AuctionResponse();
        when(auctionService.getById(any(Long.class))).thenReturn(foundAuction);
        // when & then
        mockMvc.perform(get("/api/v1/auctions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(AUCTION_RETRIEVED_SUCCESS));
    }

    @Test
    @DisplayName("검색 필터 조건 조회")
    void hello2() throws Exception {
        // given
        Page<AuctionResponse.Details> auctionDetails = new PageImpl<>(Collections.emptyList());
        when(auctionService.getAuctionsByFilter(any(Pageable.class), any(AuctionRequest.SearchCondition.class))).thenReturn(auctionDetails);
        // when & then
        mockMvc.perform(get("/api/v1/auctions")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(AUCTION_RETRIEVED_SUCCESS));

    }

    @Test
    @DisplayName("경매 수정")
    void hello3() throws Exception {
        // given
        AuctionRequest.Update request = new AuctionRequest.Update();
        AuctionResponse updatedAuction = new AuctionResponse();
        when(auctionService.update(any(Long.class),any(AuctionRequest.Update.class))).thenReturn(updatedAuction);
        // when & then
        mockMvc.perform(put("/api/v1/auctions/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(AUCTION_UPDATED_SUCCESS));

    }
//    @Test
//    @DisplayName("경매 삭제")
//    void hello () {
//        // given
//
//        mockMvc.perform(delete())
//        // when & then
//
//
//    }
}