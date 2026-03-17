package com.example.sum_gil_be.dashboard.client;

import com.example.sum_gil_be.dashboard.config.KakaoLocalProperties;
import com.example.sum_gil_be.dashboard.domain.dto.kakao.KakaoCoord2RegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final KakaoLocalProperties properties;

    public KakaoCoord2RegionResponse coordToRegion(double lat, double lng) {
        RestClient restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + properties.restApiKey())
                .build();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/geo/coord2regioncode.json")
                        .queryParam("x", lng)   // 경도
                        .queryParam("y", lat)   // 위도
                        .queryParam("input_coord", "WGS84")
                        .build())
                .retrieve()
                .body(KakaoCoord2RegionResponse.class);
    }
}