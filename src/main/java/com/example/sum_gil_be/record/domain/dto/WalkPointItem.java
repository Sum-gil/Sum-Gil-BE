package com.example.sum_gil_be.record.domain.dto;

import java.time.LocalDateTime;

public record WalkPointItem(
        Double latitude,
        Double longitude,
        Integer sequence,
        LocalDateTime recordedAt
) {
}