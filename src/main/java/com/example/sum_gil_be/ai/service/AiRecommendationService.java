package com.example.sum_gil_be.ai.service;

import com.example.sum_gil_be.ai.domain.dto.AiRecommendationRequest;
import com.example.sum_gil_be.ai.domain.dto.AiRecommendationResponse;
import com.example.sum_gil_be.ai.domain.dto.ParsedRecommendationCondition;
import com.example.sum_gil_be.ai.domain.dto.RecommendedPlaceDto;
import com.example.sum_gil_be.walkspot.domain.dto.HealthScoreResponse;
import com.example.sum_gil_be.walkspot.domain.dto.InfrastructureResponse;
import com.example.sum_gil_be.walkspot.domain.dto.PlaceListResponse;
import com.example.sum_gil_be.walkspot.domain.dto.SafetyScoreResponse;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import com.example.sum_gil_be.walkspot.service.PlaceScoreService;
import com.example.sum_gil_be.walkspot.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final OpenAiQueryParserService openAiQueryParserService;
    private final PlaceService placeService;
    private final PlaceScoreService placeScoreService;
    private final WalkSpotRepository walkSpotRepository;

    public AiRecommendationResponse recommend(AiRecommendationRequest request) {
        validateRequest(request);

        ParsedRecommendationCondition condition =
                openAiQueryParserService.parse(request.userInput());

        double effectiveRadius = request.radius() != null
                ? request.radius()
                : Optional.ofNullable(condition.radiusMeters()).orElse(3000);

        int limit = Optional.ofNullable(condition.requestedCount()).orElse(3);
        limit = Math.min(Math.max(limit, 1), 5);

        // 여기 핵심: 기본 3개용이 아니라 AI 전용 후보 조회 사용
        List<PlaceListResponse> nearbyPlaces =
                placeService.getNearbyPlacesForAi(request.latitude(), request.longitude(), effectiveRadius);

        if (nearbyPlaces == null || nearbyPlaces.isEmpty()) {
            return new AiRecommendationResponse(
                    request.userInput(),
                    "주변에서 추천 가능한 산책 장소를 찾지 못했습니다.",
                    condition,
                    List.of()
            );
        }

        // 먼저 가까운 후보 일부만 추려서 무거운 계산 수 줄이기
        List<PlaceListResponse> candidatePlaces = nearbyPlaces.stream()
                .sorted(Comparator.comparingDouble(place ->
                        place.getDistance() != null ? place.getDistance() : Double.MAX_VALUE))
                .limit(10)
                .toList();

        Map<Long, WalkSpot> walkSpotMap = walkSpotRepository.findAllById(
                candidatePlaces.stream()
                        .map(PlaceListResponse::getPlaceId)
                        .toList()
        ).stream().collect(Collectors.toMap(WalkSpot::getId, walkSpot -> walkSpot));

        List<RecommendedPlaceDto> recommendations = candidatePlaces.stream()
                .map(place -> toRecommendedPlace(
                        place,
                        walkSpotMap.get(place.getPlaceId()),
                        condition,
                        effectiveRadius
                ))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(RecommendedPlaceDto::recommendationScore).reversed())
                .limit(limit)
                .toList();

        return new AiRecommendationResponse(
                request.userInput(),
                buildSummary(condition),
                condition,
                recommendations
        );
    }

    private RecommendedPlaceDto toRecommendedPlace(
            PlaceListResponse place,
            WalkSpot walkSpot,
            ParsedRecommendationCondition condition,
            double radius
    ) {
        if (walkSpot == null) {
            return null;
        }

        HealthScoreResponse health = placeScoreService.getHealthScore(place.getPlaceId());
        SafetyScoreResponse safety = placeScoreService.getSafetyScore(place.getPlaceId());

        boolean needInfrastructureCheck =
                Boolean.TRUE.equals(condition.needCafe()) || Boolean.TRUE.equals(condition.needToilet());

        List<InfrastructureResponse> infrastructures = List.of();
        boolean hasCafe = false;
        boolean hasToilet = false;

        if (needInfrastructureCheck) {
            infrastructures = placeService.getInfrastructures(place.getPlaceId(), null);

            hasCafe = infrastructures.stream()
                    .map(infra -> String.valueOf(infra.getType()))
                    .anyMatch(type -> "CAFE".equalsIgnoreCase(type));

            hasToilet = infrastructures.stream()
                    .map(infra -> String.valueOf(infra.getType()))
                    .anyMatch(type -> "TOILET".equalsIgnoreCase(type));
        }

        int healthScore = health != null && health.getHealthScore() != null ? health.getHealthScore() : 0;
        int safetyScore = safety != null && safety.getSafetyScore() != null ? safety.getSafetyScore() : 0;
        double distance = place.getDistance() != null ? place.getDistance() : radius;

        double recommendationScore = calculateRecommendationScore(
                distance,
                radius,
                healthScore,
                safetyScore,
                walkSpot,
                hasCafe,
                hasToilet,
                condition
        );

        return new RecommendedPlaceDto(
                walkSpot.getId(),
                walkSpot.getName(),
                walkSpot.getAddress(),
                walkSpot.getLatitude(),
                walkSpot.getLongitude(),
                distance,
                healthScore,
                safetyScore,
                walkSpot.getNightSafe(),
                buildReason(
                        place,
                        walkSpot,
                        healthScore,
                        safetyScore,
                        hasCafe,
                        hasToilet,
                        condition
                ),
                round(recommendationScore)
        );
    }

    private double calculateRecommendationScore(
            double distance,
            double radius,
            int healthScore,
            int safetyScore,
            WalkSpot walkSpot,
            boolean hasCafe,
            boolean hasToilet,
            ParsedRecommendationCondition condition
    ) {
        double score = 0.0;

        double distanceScore = calculateDistanceScore(distance, radius);
        score += distanceScore * 0.35;

        score += safetyScore * (Boolean.TRUE.equals(condition.preferSafe()) ? 0.35 : 0.20);

        score += healthScore * (
                Boolean.TRUE.equals(condition.preferCleanAir())
                        || Boolean.TRUE.equals(condition.preferGreen())
                        || Boolean.TRUE.equals(condition.preferLowCrowd())
                        ? 0.35 : 0.20
        );

        if (Boolean.TRUE.equals(condition.preferNightWalk())
                && Boolean.TRUE.equals(walkSpot.getNightSafe())) {
            score += 15;
        }

        if (Boolean.TRUE.equals(condition.preferGreen())
                && walkSpot.getGreenRatio() != null) {
            score += walkSpot.getGreenRatio() * 0.15;
        }

        if (Boolean.TRUE.equals(condition.needCafe())) {
            score += hasCafe ? 10 : -8;
        }

        if (Boolean.TRUE.equals(condition.needToilet())) {
            score += hasToilet ? 10 : -8;
        }

        if (condition.regionKeyword() != null && !condition.regionKeyword().isBlank()) {
            String keyword = condition.regionKeyword().toLowerCase();
            String target = (
                    nullToBlank(walkSpot.getName()) + " " +
                    nullToBlank(walkSpot.getAddress()) + " " +
                    nullToBlank(walkSpot.getRegion())
            ).toLowerCase();

            if (target.contains(keyword)) {
                score += 20;
            }
        }

        return score;
    }

    private double calculateDistanceScore(double distance, double radius) {
        if (radius <= 0) {
            return 0;
        }

        double ratio = Math.min(distance / radius, 1.0);
        return 100 * (1 - ratio);
    }

    private String buildReason(
            PlaceListResponse place,
            WalkSpot walkSpot,
            int healthScore,
            int safetyScore,
            boolean hasCafe,
            boolean hasToilet,
            ParsedRecommendationCondition condition
    ) {
        List<String> reasons = new ArrayList<>();

        if (place.getDistance() != null) {
            reasons.add(String.format("현재 위치에서 약 %.0fm 거리입니다", place.getDistance()));
        }

        if (Boolean.TRUE.equals(condition.preferSafe())) {
            reasons.add("안전 점수가 높습니다(" + safetyScore + "점)");
        }

        if (Boolean.TRUE.equals(condition.preferCleanAir())
                || Boolean.TRUE.equals(condition.preferGreen())
                || Boolean.TRUE.equals(condition.preferLowCrowd())) {
            reasons.add("건강 점수가 우수합니다(" + healthScore + "점)");
        }

        if (Boolean.TRUE.equals(condition.preferNightWalk())
                && Boolean.TRUE.equals(walkSpot.getNightSafe())) {
            reasons.add("야간 산책에 비교적 적합합니다");
        }

        if (Boolean.TRUE.equals(condition.needCafe()) && hasCafe) {
            reasons.add("주변 카페 접근성이 좋습니다");
        }

        if (Boolean.TRUE.equals(condition.needToilet()) && hasToilet) {
            reasons.add("주변 화장실 접근성이 좋습니다");
        }

        if (Boolean.TRUE.equals(condition.preferGreen())
                && walkSpot.getGreenRatio() != null) {
            reasons.add("녹지 비율이 반영되었습니다(" + walkSpot.getGreenRatio() + ")");
        }

        if (reasons.isEmpty()) {
            reasons.add("현재 위치와 종합 점수를 기준으로 추천했습니다");
        }

        return String.join(", ", reasons) + ".";
    }

    private String buildSummary(ParsedRecommendationCondition condition) {
        List<String> keywords = new ArrayList<>();

        if (Boolean.TRUE.equals(condition.preferSafe())) keywords.add("안전");
        if (Boolean.TRUE.equals(condition.preferCleanAir())) keywords.add("공기질");
        if (Boolean.TRUE.equals(condition.preferLowCrowd())) keywords.add("혼잡도");
        if (Boolean.TRUE.equals(condition.preferGreen())) keywords.add("녹지");
        if (Boolean.TRUE.equals(condition.preferNightWalk())) keywords.add("야간 산책");
        if (Boolean.TRUE.equals(condition.needCafe())) keywords.add("카페");
        if (Boolean.TRUE.equals(condition.needToilet())) keywords.add("화장실");

        if (keywords.isEmpty()) {
            return "현재 위치와 기본 점수를 기준으로 추천했습니다.";
        }

        return "현재 위치를 기준으로 " + String.join(", ", keywords) + " 조건을 반영해 추천했습니다.";
    }

    private void validateRequest(AiRecommendationRequest request) {
        if (request.userInput() == null || request.userInput().isBlank()) {
            throw new IllegalArgumentException("userInput은 필수입니다.");
        }

        if (request.latitude() == null || request.longitude() == null) {
            throw new IllegalArgumentException("latitude, longitude는 필수입니다.");
        }

        if (request.radius() != null && request.radius() <= 0) {
            throw new IllegalArgumentException("radius는 0보다 커야 합니다.");
        }
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}