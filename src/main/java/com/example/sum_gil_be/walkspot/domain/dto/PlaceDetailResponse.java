package com.example.sum_gil_be.walkspot.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetailResponse {

    private Long placeId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private String region;
    private Integer safetyScore;
}