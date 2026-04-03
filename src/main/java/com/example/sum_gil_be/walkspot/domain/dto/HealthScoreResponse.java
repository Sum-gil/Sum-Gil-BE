package com.example.sum_gil_be.walkspot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HealthScoreResponse {
    private Long placeId;
    private String placeName;
    private Integer healthScore;
    private String grade;
    private String message;

    private String administrativeDistrict;
    private Integer airQualityScore;
    private Integer greenRatio;
    private Integer visitorCount;
}