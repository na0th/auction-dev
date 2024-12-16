package com.example.na0th.auction.api.auction;

import com.example.na0th.auction.common.constant.ApiResponseMessages;
import com.example.na0th.auction.common.response.ApiResult;
import com.example.na0th.auction.domain.auction.dto.request.AuctionRequest;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.*;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    /**
    post에서 RequestParam으로 쿼리 스트링으로 userId를 보내는 것이 어색하지만,
    추후에 jwt에서 userId를 추출할 것이기에 분리해둔 것..
    */
    @PostMapping
    public ResponseEntity<ApiResult<AuctionResponse>> create(@RequestParam Long userId, @RequestBody AuctionRequest.Create create) {
        AuctionResponse createdAuction = auctionService.create(userId, create);
        return ResponseEntity.ok(ApiResult.success(AUCTION_CREATED_SUCCESS, createdAuction));
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<ApiResult<AuctionResponse>> get(@PathVariable Long auctionId) {
        AuctionResponse foundAuction = auctionService.getById(auctionId);
        return ResponseEntity.ok(ApiResult.success(AUCTION_RETRIEVED_SUCCESS, foundAuction));
    }

    @PutMapping("/{auctionId}")
    public ResponseEntity<ApiResult<AuctionResponse>> update(@PathVariable Long auctionId, @RequestBody AuctionRequest.Update request) {
        AuctionResponse updatedAuction = auctionService.update(auctionId, request);
        return ResponseEntity.ok(ApiResult.success(AUCTION_UPDATED_SUCCESS, updatedAuction));
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<ApiResult<AuctionResponse>> delete(@PathVariable Long auctionId) {
        auctionService.delete(auctionId);
        return ResponseEntity.ok(ApiResult.success(AUCTION_DELETED_SUCCESS));
    }

}
