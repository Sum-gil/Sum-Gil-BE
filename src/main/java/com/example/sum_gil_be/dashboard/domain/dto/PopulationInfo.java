package com.example.sum_gil_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopulationInfo {
    private String administrativeDistrict;
    private Integer visitorCount;
    private String sensingTime;
}