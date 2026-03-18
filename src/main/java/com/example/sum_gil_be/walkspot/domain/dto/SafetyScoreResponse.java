package com.example.sum_gil_be.walkspot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SafetyScoreResponse {
    private Long placeId;
    private String placeName;
    private Integer safetyScore;
    private String grade;
    private String message;

    private String administrativeDistrict;
    private Integer visitorCount;
    private Boolean nightSafe;
    private Integer nearbyCctvCount;
    private Integer cctvScore;
}