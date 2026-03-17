package com.example.sum_gil_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WeatherInfo {

    private Double temperature;
    private Integer humidity;
    private Boolean precipitation;
}