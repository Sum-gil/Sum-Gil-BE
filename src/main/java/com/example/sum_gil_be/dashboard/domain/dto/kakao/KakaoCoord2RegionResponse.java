package com.example.sum_gil_be.dashboard.domain.dto.kakao;

import java.util.List;

public record KakaoCoord2RegionResponse(
        Meta meta,
        List<Document> documents
) {
    public record Meta(
            int total_count
    ) {
    }

    public record Document(
            String region_type,
            String address_name,
            String region_1depth_name,
            String region_2depth_name,
            String region_3depth_name,
            String region_4depth_name,
            String code,
            Double x,
            Double y
    ) {
    }
}