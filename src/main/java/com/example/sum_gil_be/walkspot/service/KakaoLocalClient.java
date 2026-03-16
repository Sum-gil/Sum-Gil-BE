package com.example.sum_gil_be.walkspot.service;

import com.example.sum_gil_be.walkspot.config.KakaoLocalProperties;
import com.example.sum_gil_be.walkspot.domain.dto.KakaoPlaceSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final KakaoLocalProperties kakaoLocalProperties;

    private static final String BASE_URL = "https://dapi.kakao.com";

    public KakaoPlaceSearchResponse searchByCategory(String categoryCode, double longitude, double latitude, int radius) {
        RestClient restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoLocalProperties.restApiKey())
                .build();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/category.json")
                        .queryParam("category_group_code", categoryCode)
                        .queryParam("x", longitude)
                        .queryParam("y", latitude)
                        .queryParam("radius", radius)
                        .queryParam("sort", "distance")
                        .queryParam("size", 15)
                        .build())
                .retrieve()
                .body(KakaoPlaceSearchResponse.class);
    }

    public KakaoPlaceSearchResponse searchByKeyword(String query, double longitude, double latitude, int radius) {
        RestClient restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoLocalProperties.restApiKey())
                .build();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .queryParam("x", longitude)
                        .queryParam("y", latitude)
                        .queryParam("radius", radius)
                        .queryParam("sort", "distance")
                        .queryParam("size", 15)
                        .build())
                .retrieve()
                .body(KakaoPlaceSearchResponse.class);
    }
}