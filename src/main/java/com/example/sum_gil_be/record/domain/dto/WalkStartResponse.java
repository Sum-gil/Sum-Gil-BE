package com.example.sum_gil_be.record.domain.dto;

import java.time.LocalDateTime;

public record WalkStartResponse(
        Long walkRecordId,
        LocalDateTime startedAt,
        String status
) {
}