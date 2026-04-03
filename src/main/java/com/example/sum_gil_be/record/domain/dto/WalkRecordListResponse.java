package com.example.sum_gil_be.record.domain.dto;

import java.time.LocalDateTime;

public record WalkRecordListResponse(
        Long walkRecordId,
        Long walkSpotId,
        String walkSpotName,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Double totalDistance,
        Integer durationSeconds,
        Double calories,
        Integer averageHealthScore,
        String status
) {
}