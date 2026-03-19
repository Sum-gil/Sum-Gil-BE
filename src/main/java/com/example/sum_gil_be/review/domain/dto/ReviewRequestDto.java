package com.example.sum_gil_be.review.domain.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long rating;
    private String content;
    private Long walkSpotId; 
}