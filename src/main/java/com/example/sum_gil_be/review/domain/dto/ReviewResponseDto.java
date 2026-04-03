package com.example.sum_gil_be.review.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private Long walkSpotId;
    private String walkSpotName;

    private Long rating;
    private String content;
    private LocalDateTime createdAt;

    private String nickname;
}