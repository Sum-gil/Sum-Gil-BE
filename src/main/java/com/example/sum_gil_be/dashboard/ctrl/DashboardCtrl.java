package com.example.sum_gil_be.dashboard.ctrl;

import com.example.sum_gil_be.dashboard.domain.dto.EnvironmentResponse;
import com.example.sum_gil_be.dashboard.domain.dto.RecommendationResponse;
import com.example.sum_gil_be.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard API", description = "메인 / 환경 정보 API")
public class DashboardCtrl {

    private final DashboardService dashboardService;

    @GetMapping("/environment")
    @Operation(summary = "현재 산책 환경 조회", description = "현재 위치의 날씨 및 대기질 정보를 조회합니다.")
    public ResponseEntity<EnvironmentResponse> getEnvironment(
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        return ResponseEntity.ok(dashboardService.getEnvironment(lat, lng));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "지역 기반 추천 산책 장소 조회", description = "현재 region과 같은 지역의 산책 장소를 우선 추천합니다.")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(dashboardService.getRecommendations(lat, lng, limit));
    }
}