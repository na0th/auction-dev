package com.example.na0th.auction.api.user;

import com.example.na0th.auction.common.constant.ApiResponseMessages;
import com.example.na0th.auction.common.response.ApiResult;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponse;
import com.example.na0th.auction.domain.auction.dto.response.AuctionResponseWon;
import com.example.na0th.auction.domain.auction.service.AuctionService;
import com.example.na0th.auction.domain.user.dto.request.UserRequest;
import com.example.na0th.auction.domain.user.dto.response.UserResponse;
import com.example.na0th.auction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.USER_WON_AUCTIONS_RETRIEVED_SUCCESS;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuctionService auctionService;
    @PostMapping
    public ResponseEntity<ApiResult<UserResponse>> create(@RequestBody UserRequest.Create request) {
        UserResponse createdUser = userService.create(request);
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.USER_CREATED_SUCCESS, createdUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResult<UserResponse>> get(@PathVariable Long userId) {
        UserResponse foundUser = userService.getById(userId);
        return ResponseEntity.ok(ApiResult.success(null, foundUser));
    }

    @GetMapping("/{userId}/auctions")
    public ResponseEntity<ApiResult<Page<AuctionResponse.Won>>> getWonAuctions(@PathVariable Long userId, Pageable pageable) {
        Page<AuctionResponse.Won> auctions = auctionService.findWonAuctionsByUserIdV2(userId, pageable);
        return ResponseEntity.ok(ApiResult.success(USER_WON_AUCTIONS_RETRIEVED_SUCCESS,auctions));
    }
//    @GetMapping("/{userId}/auctions")
//    public ResponseEntity<ApiResult<Page<AuctionResponseWon>>> getWonAuctions(@PathVariable Long userId, Pageable pageable) {
//        Page<AuctionResponseWon> auctions = auctionService.findWonAuctionsByUserIdV3(userId, pageable);
//        return ResponseEntity.ok(ApiResult.success(USER_WON_AUCTIONS_RETRIEVED_SUCCESS,auctions));
//    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResult<UserResponse>> update(@PathVariable Long userId, @RequestBody UserRequest.Update request) {
        UserResponse updatedUser = userService.update(userId, request);
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.USER_UPDATED_SUCCESS, updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResult<UserResponse>> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.USER_DELETED_SUCCESS));
    }
}
