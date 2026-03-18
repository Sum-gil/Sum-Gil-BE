package com.example.sum_gil_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecommendedPlaceResponse {

    private Long placeId;
    private String name;
    private String address;
    private Double distance;
    private Integer safetyScore;
    private String recommendReason;
}