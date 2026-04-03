package com.example.sum_gil_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AirQualityInfo {

    private Integer pm10;
    private Integer pm25;
}