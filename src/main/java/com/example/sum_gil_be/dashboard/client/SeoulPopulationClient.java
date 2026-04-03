package com.example.sum_gil_be.dashboard.client;

import com.example.sum_gil_be.dashboard.domain.dto.SeoulPopulationApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SeoulPopulationClient {

    @Value("${external.seoul-population.api-key}")
    private String apiKey;

    @Value("${external.seoul-population.base-url}")
    private String baseUrl;

    @Value("${external.seoul-population.service-name}")
    private String serviceName;

    private final RestTemplate restTemplate = new RestTemplate();

    public SeoulPopulationApiResponse fetchPopulation() {
        String url = baseUrl + "/" + apiKey + "/json/" + serviceName + "/1/1000";
        return restTemplate.getForObject(url, SeoulPopulationApiResponse.class);
    }
}