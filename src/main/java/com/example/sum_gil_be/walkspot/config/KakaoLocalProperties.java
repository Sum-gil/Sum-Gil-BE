package com.example.sum_gil_be.walkspot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.local")
public record KakaoLocalProperties(
        String restApiKey
) {
}