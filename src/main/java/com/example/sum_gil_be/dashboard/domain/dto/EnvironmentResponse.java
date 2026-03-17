package com.example.sum_gil_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EnvironmentResponse {

    private String region;
    private Double temperature;
    private Integer humidity;
    private Integer pm10;
    private Integer pm25;
    private Boolean precipitation;
    private Boolean walkable;
    private String status;
    private String message;
}