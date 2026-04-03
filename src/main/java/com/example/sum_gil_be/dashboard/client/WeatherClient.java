package com.example.sum_gil_be.dashboard.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class WeatherClient {

    @Value("${external.weather.base-url}")
    private String baseUrl;

    @Value("${external.weather.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getUltraSrtNcst(String baseDate, String baseTime, int nx, int ny) {

    String url = UriComponentsBuilder
            .fromUriString(baseUrl + "/getUltraSrtNcst")
            .queryParam("serviceKey", serviceKey)
            .queryParam("pageNo", 1)
            .queryParam("numOfRows", 1000)
            .queryParam("dataType", "JSON")
            .queryParam("base_date", baseDate)
            .queryParam("base_time", baseTime)
            .queryParam("nx", nx)
            .queryParam("ny", ny)
            .build()
            .toUriString();

    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    return response.getBody();
}
}