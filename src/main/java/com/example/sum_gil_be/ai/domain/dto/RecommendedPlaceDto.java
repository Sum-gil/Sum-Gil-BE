package com.example.sum_gil_be.ai.domain.dto;

public record RecommendedPlaceDto(
        Long placeId,
        String name,
        String address,
        Double latitude,
        Double longitude,
        Double distance,
        Integer healthScore,
        Integer safetyScore,
        Boolean nightSafe,
        String reason,
        Double recommendationScore
) {
}