package com.example.na0th.auction.domain.bid.exception;

public class NotHighestBidException extends RuntimeException {
    public NotHighestBidException(String message) {
        super(message);
    }
}
