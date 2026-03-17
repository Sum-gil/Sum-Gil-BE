package com.example.sum_gil_be.dashboard.util;

public class RegionMapper {

    private RegionMapper() {
    }

    public static String getSidoName(double lat, double lng) {
        // TODO: 실제 좌표 -> 시도명 변환으로 교체
        return "경기";
    }

    public static String getRegionName(double lat, double lng) {
        // TODO: 실제 좌표 -> 지역명 변환으로 교체
        // 일단 WalkSpot.region 값과 맞춰서 테스트용으로 사용
        return "수원";
    }
}