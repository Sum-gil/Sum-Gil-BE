package com.example.sum_gil_be.ai.domain.dto;

public record AiRecommendationRequest(
        String userInput,
        Double latitude,
        Double longitude,
        Double radius
) {
}