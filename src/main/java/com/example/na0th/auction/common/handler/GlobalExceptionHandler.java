package com.example.na0th.auction.common.handler;

import com.example.na0th.auction.common.constant.ApiResponseMessages;
import com.example.na0th.auction.common.response.ApiResult;
import com.example.na0th.auction.domain.auction.exception.AuctionCanNotUpdateException;
import com.example.na0th.auction.domain.auction.exception.AuctionNotFoundException;
import com.example.na0th.auction.domain.auction.exception.InvalidAuctionTimeException;
import com.example.na0th.auction.domain.product.exception.ProductNotFoundException;
import com.example.na0th.auction.domain.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuctionCanNotUpdateException.class)
    public ResponseEntity<ApiResult<String>> handleAuctionCanNotUpdateException(AuctionCanNotUpdateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.failure(e.getMessage()));
    }

    @ExceptionHandler(InvalidAuctionTimeException.class)
    public ResponseEntity<ApiResult<String>> handleInvalidAuctionTimeException(InvalidAuctionTimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.failure(e.getMessage()));
    }

    @ExceptionHandler(AuctionNotFoundException.class)
    public ResponseEntity<ApiResult<String>> handleAuctionNotFoundException(AuctionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.failure(AUCTION_NOT_FOUND));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResult<String>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.failure(USER_NOT_FOUND));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResult<String>> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.failure(PRODUCT_NOT_FOUND));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.failure(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResult<String>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.failure(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResult<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.failure(ex.getMessage()));
    }
}
