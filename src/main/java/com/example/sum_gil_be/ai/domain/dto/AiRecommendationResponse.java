package com.example.sum_gil_be.ai.domain.dto;

import java.util.List;

public record AiRecommendationResponse(
        String userInput,
        String summary,
        ParsedRecommendationCondition parsedCondition,
        List<RecommendedPlaceDto> recommendations
) {
}