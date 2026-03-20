package com.example.sum_gil_be.record.domain.dto;

import java.util.List;

public record WalkPointRequest(
        List<WalkPointItem> points
) {
}