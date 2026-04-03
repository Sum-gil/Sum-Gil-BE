package com.example.sum_gil_be.ai.service;

import com.example.sum_gil_be.ai.config.OpenAiConfig.OpenAiProperties;
import com.example.sum_gil_be.ai.domain.dto.ParsedRecommendationCondition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiQueryParserService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OpenAiProperties openAiProperties;

    public ParsedRecommendationCondition parse(String userInput) {
        try {
            String rawJson = callOpenAi(userInput);
            return objectMapper.readValue(rawJson, ParsedRecommendationCondition.class);
        } catch (Exception e) {
            return fallbackParse(userInput);
        }
    }

    private String callOpenAi(String userInput) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiProperties.apiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", openAiProperties.model());
        body.put("instructions", """
                당신은 산책 장소 추천용 자연어 파서다.
                사용자의 자연어 요청을 분석해서 반드시 JSON만 반환하라.
                설명 문장, 마크다운, 코드블록 없이 순수 JSON만 반환하라.

                반환 형식:
                {
                  "preferSafe": true/false,
                  "preferCleanAir": true/false,
                  "preferLowCrowd": true/false,
                  "preferGreen": true/false,
                  "preferNightWalk": true/false,
                  "needCafe": true/false,
                  "needToilet": true/false,
                  "regionKeyword": "지역명 또는 null",
                  "requestedCount": 숫자,
                  "radiusMeters": 숫자
                }

                규칙:
                - 값이 없으면 false 또는 null 사용
                - requestedCount 기본값은 3
                - radiusMeters 기본값은 3000
                - "밤", "야간", "저녁"은 preferNightWalk=true
                - "안전", "cctv"는 preferSafe=true
                - "공기", "미세먼지", "쾌적"은 preferCleanAir=true
                - "조용", "한적", "사람 적은"은 preferLowCrowd=true
                - "공원", "자연", "녹지", "나무 많은"은 preferGreen=true
                - "카페"는 needCafe=true
                - "화장실"은 needToilet=true
                - 특정 지역명이 있으면 regionKeyword에 넣어라
                """);
        body.put("input", "사용자 요청: " + userInput);
        body.put("max_output_tokens", 300);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                openAiProperties.baseUrl(),
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("OpenAI 응답이 비정상입니다.");
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        String outputText = extractOutputText(root);

        if (outputText == null || outputText.isBlank()) {
            throw new IllegalStateException("OpenAI output_text 추출 실패");
        }

        return sanitizeJson(outputText);
    }

    private String extractOutputText(JsonNode root) {
        JsonNode output = root.path("output");
        if (!output.isArray()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (JsonNode item : output) {
            JsonNode content = item.path("content");
            if (!content.isArray()) continue;

            for (JsonNode c : content) {
                JsonNode textNode = c.get("text");
                if (textNode != null && !textNode.isNull()) {
                    sb.append(textNode.asText());
                }
            }
        }

        return sb.toString().trim();
    }

    private String sanitizeJson(String text) {
        String cleaned = text.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7).trim();
        }
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3).trim();
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
        }

        return cleaned;
    }

    private ParsedRecommendationCondition fallbackParse(String userInput) {
        String input = userInput == null ? "" : userInput.toLowerCase();

        boolean preferSafe = input.contains("안전") || input.contains("cctv");
        boolean preferCleanAir = input.contains("공기") || input.contains("미세먼지") || input.contains("쾌적");
        boolean preferLowCrowd = input.contains("조용") || input.contains("한적") || input.contains("사람 적");
        boolean preferGreen = input.contains("공원") || input.contains("자연") || input.contains("녹지") || input.contains("나무");
        boolean preferNightWalk = input.contains("밤") || input.contains("야간") || input.contains("저녁");
        boolean needCafe = input.contains("카페");
        boolean needToilet = input.contains("화장실");

        return new ParsedRecommendationCondition(
                preferSafe,
                preferCleanAir,
                preferLowCrowd,
                preferGreen,
                preferNightWalk,
                needCafe,
                needToilet,
                null,
                3,
                3000
        );
    }
}