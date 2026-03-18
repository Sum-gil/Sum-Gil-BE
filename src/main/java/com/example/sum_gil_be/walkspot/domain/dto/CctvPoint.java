package com.example.sum_gil_be.walkspot.domain.dto;

public record CctvPoint(
        String purpose,
        Integer cameraCount,
        Double latitude,
        Double longitude
) {
}