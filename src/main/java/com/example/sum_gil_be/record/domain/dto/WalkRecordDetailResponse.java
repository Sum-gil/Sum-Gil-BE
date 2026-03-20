package com.example.sum_gil_be.record.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WalkRecordDetailResponse(
        Long walkRecordId,
        Long walkSpotId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Double totalDistance,
        Integer durationSeconds,
        Double calories,
        Integer averageHealthScore,
        String status,
        List<WalkPathPointResponse> pathPoints
) {
}