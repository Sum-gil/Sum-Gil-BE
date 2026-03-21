package com.example.sum_gil_be.report.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WalkReportResponse {

    private Long walkRecordId;
    private String placeName;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private Long totalDurationSeconds;
    private String totalDurationText;

    private Double totalDistanceKm;
    private Double averageSpeedKmh;

    private Double averageHealthScore;
    private Double averageSafetyScore;

    private String reportMessage;
}