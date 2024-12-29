package com.example.na0th.auction.domain.auction.service;

import com.example.na0th.auction.common.jobRunr.AuctionJob;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuctionSchedulerService {
    private final JobScheduler jobScheduler;
    private final AuctionJob auctionJob;

    // 작업 ID 관리 (수정/삭제 지원)
    private final Map<Long, UUID> startJobMap = new ConcurrentHashMap<>();
    private final Map<Long, UUID> endJobMap = new ConcurrentHashMap<>();

    // 시작 시각 작업 스케줄
    public void scheduleStart(Long auctionId, LocalDateTime startTime) {
        if (startJobMap.containsKey(auctionId)) {
            UUID jobId = startJobMap.get(auctionId);
            jobScheduler.delete(jobId); // 기존 작업 삭제
        }
        UUID jobId = jobScheduler.schedule(
                Instant.from(startTime.atZone(ZoneId.systemDefault())),
                () -> auctionJob.startAuction(auctionId)
        ).asUUID();

        startJobMap.put(auctionId, jobId);
        System.out.println("경매 시작 작업 등록 완료 - ID: " + auctionId + ", 시간: " + startTime);
    }

    // 종료 시각 작업 스케줄
    public void scheduleEnd(Long auctionId, LocalDateTime endTime) {
        // TODO 일단 넘어가지만...
        if (endJobMap.containsKey(auctionId)) {
            UUID jobId = endJobMap.get(auctionId);
            jobScheduler.delete(jobId); // 기존 작업 삭제 ??
        }
        UUID jobId = jobScheduler.schedule(
                Instant.from(endTime.atZone(ZoneId.systemDefault())),
                () -> auctionJob.endAuction(auctionId)
        ).asUUID();

        endJobMap.put(auctionId, jobId);
        System.out.println("경매 종료 작업 등록 완료 - ID: " + auctionId + ", 시간: " + endTime);
    }
}
