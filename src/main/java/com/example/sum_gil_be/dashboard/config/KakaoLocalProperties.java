package com.example.sum_gil_be.dashboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.local")
public record KakaoLocalProperties(
        String restApiKey,
        String baseUrl
) {
}