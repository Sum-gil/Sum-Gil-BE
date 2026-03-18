package com.example.sum_gil_be.dashboard.service;

import com.example.sum_gil_be.dashboard.domain.dto.EnvironmentResponse;
import com.example.sum_gil_be.dashboard.domain.dto.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EnvironmentService environmentService;
    private final RecommendationService recommendationService;

    public EnvironmentResponse getEnvironment(double lat, double lng) {
        return environmentService.getEnvironment(lat, lng);
    }

    public RecommendationResponse getRecommendations(double lat, double lng, int limit) {
        EnvironmentResponse environment = environmentService.getEnvironment(lat, lng);
        return recommendationService.getRecommendations(lat, lng, limit, environment);
    }
}