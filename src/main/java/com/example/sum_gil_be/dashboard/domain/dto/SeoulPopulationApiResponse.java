package com.example.sum_gil_be.dashboard.domain.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import java.util.Map;

@Getter
public class SeoulPopulationApiResponse {

    private Map<String, SeoulPopulationWrapper> wrapperMap;

    @JsonAnySetter
    public void setWrapperMap(String key, SeoulPopulationWrapper value) {
        this.wrapperMap = Map.of(key, value);
    }

    public SeoulPopulationWrapper getWrapper() {
        if (wrapperMap == null || wrapperMap.isEmpty()) {
            return null;
        }
        return wrapperMap.values().iterator().next();
    }

    @Getter
    public static class SeoulPopulationWrapper {
        @JsonProperty("list_total_count")
        private Integer listTotalCount;

        @JsonProperty("RESULT")
        private Result result;

        @JsonProperty("row")
        private java.util.List<SeoulPopulationRow> row;
    }

    @Getter
    public static class Result {
        @JsonProperty("CODE")
        private String code;

        @JsonProperty("MESSAGE")
        private String message;
    }
}