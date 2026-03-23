package com.example.sum_gil_be.mypage.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageSummaryResponse {

    private long favoriteCount;
    private long totalWalkCount;
    private double totalDistanceKm;
    private double totalCalories;
    private double averageHealthScore;
    private long totalDurationSeconds;
    private String totalDurationText;
}