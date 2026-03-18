package com.example.sum_gil_be.walkspot.service;

import com.example.sum_gil_be.dashboard.domain.dto.AirQualityInfo;
import com.example.sum_gil_be.dashboard.domain.dto.PopulationInfo;
import com.example.sum_gil_be.dashboard.domain.dto.ResolvedRegion;
import com.example.sum_gil_be.dashboard.service.EnvironmentService;
import com.example.sum_gil_be.dashboard.service.RegionResolveService;
import com.example.sum_gil_be.dashboard.service.SeoulPopulationService;
import com.example.sum_gil_be.walkspot.domain.dto.HealthScoreResponse;
import com.example.sum_gil_be.walkspot.domain.dto.SafetyScoreResponse;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceScoreService {

    private final WalkSpotRepository walkSpotRepository;
    private final RegionResolveService regionResolveService;
    private final SeoulPopulationService seoulPopulationService;
    private final EnvironmentService environmentService;

    public HealthScoreResponse getHealthScore(Long placeId) {
        WalkSpot place = getPlace(placeId);

        ResolvedRegion region = regionResolveService.resolve(place.getLatitude(), place.getLongitude());
        PopulationInfo populationInfo = seoulPopulationService.getPopulation(region.guName(), region.dongName());
        AirQualityInfo airQualityInfo = environmentService.getAirQualityInfo(place.getLatitude(), place.getLongitude());

        System.out.println("[HealthScore] placeId = " + placeId);
        System.out.println("[HealthScore] guName = " + region.guName());
        System.out.println("[HealthScore] dongName = " + region.dongName());
        System.out.println("[HealthScore] populationInfo = " + populationInfo);
        System.out.println("[HealthScore] visitorCount raw = " +
                (populationInfo != null ? populationInfo.getVisitorCount() : null));
        System.out.println("[HealthScore] airQualityInfo = " + airQualityInfo);

        int airQualityScore = convertAirQualityToScore(airQualityInfo);
        int greenRatio = place.getGreenRatio() == null ? 50 : place.getGreenRatio();
        int visitorCount = extractVisitorCount(populationInfo);

        int crowdScore = 100 - normalizeVisitorCount(visitorCount);

        int totalScore = (int) Math.round(
                airQualityScore * 0.5 +
                greenRatio * 0.25 +
                crowdScore * 0.25
        );

        return HealthScoreResponse.builder()
                .placeId(place.getId())
                .placeName(place.getName())
                .healthScore(totalScore)
                .grade(getGrade(totalScore))
                .message(getHealthMessage(totalScore))
                .administrativeDistrict(populationInfo != null ? populationInfo.getAdministrativeDistrict() : null)
                .airQualityScore(airQualityScore)
                .greenRatio(greenRatio)
                .visitorCount(visitorCount)
                .build();
    }

    public SafetyScoreResponse getSafetyScore(Long placeId) {
        WalkSpot place = getPlace(placeId);

        ResolvedRegion region = regionResolveService.resolve(place.getLatitude(), place.getLongitude());
        PopulationInfo populationInfo = seoulPopulationService.getPopulation(region.guName(), region.dongName());

        System.out.println("[SafetyScore] placeId = " + placeId);
        System.out.println("[SafetyScore] guName = " + region.guName());
        System.out.println("[SafetyScore] dongName = " + region.dongName());
        System.out.println("[SafetyScore] populationInfo = " + populationInfo);
        System.out.println("[SafetyScore] visitorCount raw = " +
                (populationInfo != null ? populationInfo.getVisitorCount() : null));

        int visitorCount = extractVisitorCount(populationInfo);
        boolean nightSafe = Boolean.TRUE.equals(place.getNightSafe());

        int populationSafetyScore = normalizeSafetyPopulation(visitorCount);
        int nightScore = nightSafe ? 90 : 40;

        int totalScore = (int) Math.round(
                populationSafetyScore * 0.7 +
                nightScore * 0.3
        );

        return SafetyScoreResponse.builder()
                .placeId(place.getId())
                .placeName(place.getName())
                .safetyScore(totalScore)
                .grade(getGrade(totalScore))
                .message(getSafetyMessage(totalScore))
                .administrativeDistrict(populationInfo != null ? populationInfo.getAdministrativeDistrict() : null)
                .visitorCount(visitorCount)
                .nightSafe(nightSafe)
                .build();
    }

    private WalkSpot getPlace(Long placeId) {
        return walkSpotRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다. placeId=" + placeId));
    }

    private int extractVisitorCount(PopulationInfo populationInfo) {
        if (populationInfo == null || populationInfo.getVisitorCount() == null) {
            return 0;
        }
        return populationInfo.getVisitorCount();
    }

    private int convertAirQualityToScore(AirQualityInfo airQualityInfo) {
        if (airQualityInfo == null || airQualityInfo.getPm10() == null) {
            return 60;
        }

        int pm10 = airQualityInfo.getPm10();

        if (pm10 <= 30) return 95;
        if (pm10 <= 80) return 75;
        if (pm10 <= 150) return 45;
        return 20;
    }

    private int normalizeVisitorCount(int visitorCount) {
        if (visitorCount <= 50) return 20;
        if (visitorCount <= 100) return 40;
        if (visitorCount <= 200) return 60;
        if (visitorCount <= 300) return 80;
        return 100;
    }

    private int normalizeSafetyPopulation(int visitorCount) {
        if (visitorCount <= 30) return 30;
        if (visitorCount <= 80) return 55;
        if (visitorCount <= 150) return 75;
        if (visitorCount <= 300) return 90;
        return 100;
    }

    private String getGrade(int score) {
        if (score >= 80) return "VERY_GOOD";
        if (score >= 60) return "GOOD";
        if (score >= 40) return "NORMAL";
        return "BAD";
    }

    private String getHealthMessage(int score) {
        if (score >= 80) return "산책하기 매우 좋은 건강 환경입니다.";
        if (score >= 60) return "전반적으로 무난한 산책 환경입니다.";
        if (score >= 40) return "산책은 가능하지만 혼잡도나 대기 상태를 확인하세요.";
        return "건강 점수가 낮아 주의가 필요합니다.";
    }

    private String getSafetyMessage(int score) {
        if (score >= 80) return "비교적 안전한 산책 장소입니다.";
        if (score >= 60) return "대체로 안전한 편입니다.";
        if (score >= 40) return "시간대에 따라 주의가 필요합니다.";
        return "안전도가 낮아 주의가 필요합니다.";
    }
}