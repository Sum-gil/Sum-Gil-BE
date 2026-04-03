package com.example.sum_gil_be.ai.ctrl;

import com.example.sum_gil_be.ai.domain.dto.AiRecommendationRequest;
import com.example.sum_gil_be.ai.domain.dto.AiRecommendationResponse;
import com.example.sum_gil_be.ai.service.AiRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Recommendation API", description = "자연어 기반 위치형 산책 장소 추천 API")
public class AiRecommendationCtrl {

    private final AiRecommendationService aiRecommendationService;

    @Operation(
            summary = "AI 산책 장소 추천",
            description = "사용자의 자연어 입력과 현재 위치를 기반으로 OpenAI가 조건을 해석하고, 주변 산책 장소를 추천합니다."
    )
    @PostMapping("/recommendations")
    public ResponseEntity<AiRecommendationResponse> recommend(
            @RequestBody AiRecommendationRequest request
    ) {
        return ResponseEntity.ok(aiRecommendationService.recommend(request));
    }
}