package com.example.sum_gil_be.review.domain.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {
    private Long id;
    private Long rating;
    private String content;
    private LocalDateTime createdAt;
    private String nickname; 
}