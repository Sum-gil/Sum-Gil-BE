package com.example.sum_gil_be.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAiProperties openAiProperties(
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.model:gpt-4o}") String model,
            @Value("${openai.base-url:https://api.openai.com/v1/responses}") String baseUrl
    ) {
        return new OpenAiProperties(apiKey, model, baseUrl);
    }

    public record OpenAiProperties(
            String apiKey,
            String model,
            String baseUrl
    ) {
    }
}