package com.example.sum_gil_be.dashboard.service;

import com.example.sum_gil_be.dashboard.domain.dto.EnvironmentResponse;
import com.example.sum_gil_be.dashboard.domain.dto.RecommendationResponse;
import com.example.sum_gil_be.dashboard.domain.dto.RecommendedPlaceResponse;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final WalkSpotRepository walkSpotRepository;

    public RecommendationResponse getRecommendations(double lat, double lng, int limit, EnvironmentResponse environment) {

        String region = environment.getRegion();

        List<WalkSpot> regionSpots = walkSpotRepository.findByRegionContaining(region);
        List<WalkSpot> targetSpots = regionSpots.isEmpty()
                ? walkSpotRepository.findAll()
                : regionSpots;

        List<RecommendedPlaceResponse> places = targetSpots.stream()
                .sorted(
                        Comparator.comparingDouble(
                                (WalkSpot spot) -> calculateScore(spot, lat, lng, environment, region)
                        ).reversed()
                )
                .limit(limit)
                .map((WalkSpot spot) -> RecommendedPlaceResponse.builder()
                        .placeId(spot.getId())
                        .name(spot.getName())
                        .address(spot.getAddress())
                        .distance(calculateDistance(lat, lng, spot.getLatitude(), spot.getLongitude()))
                        .safetyScore(spot.getSafetyScore())
                        .recommendReason(buildReason(environment, spot, region))
                        .build())
                .toList();

        return RecommendationResponse.builder()
                .region(region)
                .recommendedCount(places.size())
                .places(places)
                .build();
    }

    private double calculateScore(
            WalkSpot spot,
            double userLat,
            double userLng,
            EnvironmentResponse environment,
            String region
    ) {
        double score = 0.0;

        double distance = calculateDistance(userLat, userLng, spot.getLatitude(), spot.getLongitude());

        if (spot.getRegion() != null && region != null && spot.getRegion().contains(region)) {
            score += 100;
        }

        score += Math.max(0, 30 - distance);

        if (spot.getSafetyScore() != null) {
            score += spot.getSafetyScore();
        }

        if ("GOOD".equals(environment.getStatus())) {
            score += 10;
        } else if ("NORMAL".equals(environment.getStatus())) {
            score += 5;
        } else if ("BAD".equals(environment.getStatus())) {
            score -= 10;
        }

        return score;
    }

    private String buildReason(EnvironmentResponse environment, WalkSpot spot, String region) {

        boolean sameRegion = spot.getRegion() != null
                && region != null
                && spot.getRegion().contains(region);

        if ("BAD".equals(environment.getStatus())) {
            if (sameRegion) {
                return "현재 지역 안에서 비교적 가까운 산책 장소입니다.";
            }
            return "현재 환경을 고려했을 때 부담이 적은 산책 장소입니다.";
        }

        if (sameRegion && spot.getSafetyScore() != null && spot.getSafetyScore() >= 80) {
            return "현재 지역에 있으면서 안전 점수가 높은 산책로입니다.";
        }

        if (sameRegion) {
            return "현재 지역 기준으로 추천되는 산책 장소입니다.";
        }

        if (spot.getSafetyScore() != null && spot.getSafetyScore() >= 80) {
            return "안전 점수가 높은 산책로입니다.";
        }

        return "현재 환경에서 산책하기 좋은 장소입니다.";
    }

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(earthRadius * c * 10) / 10.0;
    }
}