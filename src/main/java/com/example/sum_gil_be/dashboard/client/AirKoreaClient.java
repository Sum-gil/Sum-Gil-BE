package com.example.sum_gil_be.dashboard.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AirKoreaClient {

    @Value("${external.air-korea.base-url}")
    private String baseUrl;

    @Value("${external.air-korea.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCtprvnRltmMesureDnsty(String sidoName) {

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/getCtprvnRltmMesureDnsty")
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("numOfRows", 100)
                .queryParam("pageNo", 1)
                .queryParam("sidoName", sidoName)
                .queryParam("ver", "1.0")
                .build()
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }
}