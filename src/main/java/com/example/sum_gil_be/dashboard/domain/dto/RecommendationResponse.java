package com.example.sum_gil_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecommendationResponse {

    private String region;
    private int recommendedCount;
    private List<RecommendedPlaceResponse> places;
}