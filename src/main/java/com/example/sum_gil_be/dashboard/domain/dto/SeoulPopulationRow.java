package com.example.sum_gil_be.dashboard.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeoulPopulationRow {

    @JsonProperty("MODELNAME")
    private String modelName;

    @JsonProperty("SERIAL")
    private String serial;

    @JsonProperty("SENSING_TIME")
    private String sensingTime;

    @JsonProperty("REGION")
    private String region;

    @JsonProperty("AUTONOMOUS_DISTRICT")
    private String autonomousDistrict;

    @JsonProperty("ADMINISTRATIVE_DISTRICT")
    private String administrativeDistrict;

    @JsonProperty("VISITOR_COUNT")
    private Integer visitorCount;

    @JsonProperty("DATE")
    private String date;

    @JsonProperty("DATA_NO")
    private String dataNo;
}