package com.example.na0th.auction.domain.auction.model;

import com.example.na0th.auction.domain.auction.exception.InvalidAuctionTimeException;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class AuctionTime {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    protected AuctionTime() {

    }

    private AuctionTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static AuctionTime of(LocalDateTime startTime, LocalDateTime endTime) {
        validate(startTime, endTime);
        return new AuctionTime(startTime, endTime);
    }

    private static void validate(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new InvalidAuctionTimeException("Start time and end time cannot be null.");
        }
        if (startTime.isAfter(endTime)) {
            throw new InvalidAuctionTimeException("Start time must be before end time.");
        }
    }


}
