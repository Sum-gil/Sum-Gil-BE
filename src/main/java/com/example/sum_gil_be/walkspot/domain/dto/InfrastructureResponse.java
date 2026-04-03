package com.example.sum_gil_be.walkspot.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfrastructureResponse {

    private String name;
    private String type;
    private Double latitude;
    private Double longitude;
    private Double distance;
}