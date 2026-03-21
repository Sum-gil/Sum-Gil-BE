package com.example.sum_gil_be.report.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WalkReportResponse {

    private Long walkRecordId;
    private Long walkSpotId;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private Integer totalDurationSeconds;
    private String totalDurationText;

    private Double totalDistanceKm;
    private Double averageSpeedKmh;

    private Double calories;
    private Integer averageHealthScore;

    private String reportMessage;
}