package com.example.sum_gil_be.record.domain.dto;

public record WalkEndRequest(
        Double totalDistance,
        Integer durationSeconds,
        Double calories,
        Integer averageHealthScore
) {
}