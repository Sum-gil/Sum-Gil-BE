package com.example.sum_gil_be.dashboard.service;

import com.example.sum_gil_be.dashboard.client.AirKoreaClient;
import com.example.sum_gil_be.dashboard.client.WeatherClient;
import com.example.sum_gil_be.dashboard.domain.dto.AirQualityInfo;
import com.example.sum_gil_be.dashboard.domain.dto.EnvironmentResponse;
import com.example.sum_gil_be.dashboard.domain.dto.WeatherInfo;
import com.example.sum_gil_be.dashboard.domain.dto.ResolvedRegion;
import com.example.sum_gil_be.dashboard.util.GridPoint;
import com.example.sum_gil_be.dashboard.util.WeatherBaseTimeUtils;
import com.example.sum_gil_be.dashboard.util.WeatherGridConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final WeatherClient weatherClient;
    private final AirKoreaClient airKoreaClient;
    private final ObjectMapper objectMapper;
    private final RegionResolveService regionResolveService;

    public EnvironmentResponse getEnvironment(double lat, double lng) {
        GridPoint grid = WeatherGridConverter.toGrid(lat, lng);

        LocalDateTime now = LocalDateTime.now();
        String baseDate = WeatherBaseTimeUtils.getBaseDate(now);
        String baseTime = WeatherBaseTimeUtils.getBaseTime(now);

        String weatherJson = weatherClient.getUltraSrtNcst(baseDate, baseTime, grid.getNx(), grid.getNy());

        ResolvedRegion resolvedRegion = regionResolveService.resolve(lat, lng);
        String sidoName = resolvedRegion.sidoName();
        String regionName = resolvedRegion.guName();

        String airJson = airKoreaClient.getCtprvnRltmMesureDnsty(sidoName);

        WeatherInfo weatherInfo = parseWeather(weatherJson);
        AirQualityInfo airQualityInfo = parseAirQuality(airJson);

        boolean walkable = isWalkable(weatherInfo, airQualityInfo);
        String status = calculateStatus(weatherInfo, airQualityInfo);
        String message = buildMessage(status, weatherInfo, airQualityInfo);

        return EnvironmentResponse.builder()
                .region(regionName)
                .temperature(weatherInfo.getTemperature())
                .humidity(weatherInfo.getHumidity())
                .pm10(airQualityInfo.getPm10())
                .pm25(airQualityInfo.getPm25())
                .precipitation(weatherInfo.getPrecipitation())
                .walkable(walkable)
                .status(status)
                .message(message)
                .build();
    }

    private WeatherInfo parseWeather(String json) {
        try {
            JsonNode items = objectMapper.readTree(json)
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            Double temperature = 0.0;
            Integer humidity = 0;
            Boolean precipitation = false;

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                String obsrValue = item.path("obsrValue").asText();

                if ("T1H".equals(category)) {
                    temperature = parseDouble(obsrValue, 0.0);
                } else if ("REH".equals(category)) {
                    humidity = parseInt(obsrValue, 0);
                } else if ("RN1".equals(category)) {
                    precipitation = parseDouble(obsrValue, 0.0) > 0;
                }
            }

            return WeatherInfo.builder()
                    .temperature(temperature)
                    .humidity(humidity)
                    .precipitation(precipitation)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("기상청 응답 파싱 실패", e);
        }
    }

    private AirQualityInfo parseAirQuality(String json) {
        try {
            JsonNode items = objectMapper.readTree(json)
                    .path("response")
                    .path("body")
                    .path("items");

            Integer pm10 = 0;
            Integer pm25 = 0;

            if (items.isArray() && items.size() > 0) {
                JsonNode first = items.get(0);
                pm10 = parseNullableInt(first.path("pm10Value").asText());
                pm25 = parseNullableInt(first.path("pm25Value").asText());
            }

            return AirQualityInfo.builder()
                    .pm10(pm10 == null ? 0 : pm10)
                    .pm25(pm25 == null ? 0 : pm25)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("에어코리아 응답 파싱 실패", e);
        }
    }

    private boolean isWalkable(WeatherInfo weather, AirQualityInfo air) {
        if (Boolean.TRUE.equals(weather.getPrecipitation())) {
            return false;
        }
        if (air.getPm10() >= 81 || air.getPm25() >= 36) {
            return false;
        }
        return true;
    }

    private String calculateStatus(WeatherInfo weather, AirQualityInfo air) {
        if (Boolean.TRUE.equals(weather.getPrecipitation())) {
            return "BAD";
        }
        if (air.getPm10() >= 81 || air.getPm25() >= 36) {
            return "BAD";
        }
        if (air.getPm10() >= 31 || air.getPm25() >= 16) {
            return "NORMAL";
        }
        return "GOOD";
    }

    private String buildMessage(String status, WeatherInfo weather, AirQualityInfo air) {
        if ("BAD".equals(status)) {
            if (Boolean.TRUE.equals(weather.getPrecipitation())) {
                return "현재 비가 오고 있어 산책을 추천하지 않습니다.";
            }
            return "현재 대기질이 좋지 않아 산책을 추천하지 않습니다.";
        }
        if ("NORMAL".equals(status)) {
            return "산책은 가능하지만 가볍게 짧게 걷는 것을 추천합니다.";
        }
        return "현재 산책하기 좋은 환경입니다.";
    }

    private Integer parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Integer parseNullableInt(String value) {
        try {
            if (value == null || value.isBlank() || "-".equals(value)) {
                return null;
            }
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    public AirQualityInfo getAirQualityInfo(double lat, double lng) {
        ResolvedRegion resolvedRegion = regionResolveService.resolve(lat, lng);
        String sidoName = resolvedRegion.sidoName();

        String airJson = airKoreaClient.getCtprvnRltmMesureDnsty(sidoName);
        return parseAirQuality(airJson);
    }
}