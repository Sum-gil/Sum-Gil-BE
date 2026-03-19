package com.example.sum_gil_be.ai.domain.dto;

public record ParsedRecommendationCondition(
        Boolean preferSafe,
        Boolean preferCleanAir,
        Boolean preferLowCrowd,
        Boolean preferGreen,
        Boolean preferNightWalk,
        Boolean needCafe,
        Boolean needToilet,
        String regionKeyword,
        Integer requestedCount,
        Integer radiusMeters
) {
}