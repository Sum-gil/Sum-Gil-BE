package com.example.sum_gil_be.report.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyReportResponse {

    private Integer year;
    private Integer month;

    private Integer totalWalkCount;
    private Double totalDistanceKm;
    private Long totalDurationSeconds;
    private String totalDurationText;

    private Long averageDurationSeconds;
    private String averageDurationText;

    private Double averageHealthScore;
}