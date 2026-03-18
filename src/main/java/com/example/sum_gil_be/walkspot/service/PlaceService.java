package com.example.sum_gil_be.walkspot.service;

import com.example.sum_gil_be.walkspot.domain.dto.InfrastructureResponse;
import com.example.sum_gil_be.walkspot.domain.dto.KakaoPlaceSearchResponse;
import com.example.sum_gil_be.walkspot.domain.dto.PlaceDetailResponse;
import com.example.sum_gil_be.walkspot.domain.dto.PlaceListResponse;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final WalkSpotRepository walkSpotRepository;
    private final KakaoLocalSearchClient kakaoLocalClient;

    public List<PlaceListResponse> getNearbyPlaces(double latitude, double longitude, double radius) {
        List<WalkSpot> walkSpots = walkSpotRepository.findAll();
        List<PlaceListResponse> result = new ArrayList<>();

        for (WalkSpot walkSpot : walkSpots) {
            double distance = calculateDistanceMeter(
                    latitude,
                    longitude,
                    walkSpot.getLatitude(),
                    walkSpot.getLongitude()
            );

            result.add(
                    PlaceListResponse.builder()
                            .placeId(walkSpot.getId())
                            .name(walkSpot.getName())
                            .address(walkSpot.getAddress())
                            .latitude(walkSpot.getLatitude())
                            .longitude(walkSpot.getLongitude())
                            .distance(Math.round(distance * 10) / 10.0)
                            .region(walkSpot.getRegion())
                            .safetyScore(walkSpot.getSafetyScore())
                            .build()
            );
        }

        result.sort(Comparator.comparingDouble(PlaceListResponse::getDistance));

        List<PlaceListResponse> filtered = result.stream()
                .filter(place -> place.getDistance() <= radius)
                .toList();

        if (!filtered.isEmpty()) {
            return filtered;
        }

        return result.stream().limit(3).toList();
    }

    public PlaceDetailResponse getPlaceDetail(Long placeId) {
        WalkSpot walkSpot = walkSpotRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책 장소를 찾을 수 없습니다. placeId=" + placeId));

        return PlaceDetailResponse.builder()
                .placeId(walkSpot.getId())
                .name(walkSpot.getName())
                .address(walkSpot.getAddress())
                .latitude(walkSpot.getLatitude())
                .longitude(walkSpot.getLongitude())
                .description(walkSpot.getDescription())
                .region(walkSpot.getRegion())
                .safetyScore(walkSpot.getSafetyScore())
                .build();
    }

    public List<InfrastructureResponse> getInfrastructures(Long placeId, String type) {
        WalkSpot walkSpot = walkSpotRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책 장소를 찾을 수 없습니다. placeId=" + placeId));

        int radius = 2000;

        KakaoPlaceSearchResponse response;

        if (type == null || type.isBlank()) {
            List<InfrastructureResponse> merged = new ArrayList<>();
            merged.addAll(fetchByType("CAFE", walkSpot.getLongitude(), walkSpot.getLatitude(), radius));
            merged.addAll(fetchByType("CONVENIENCE_STORE", walkSpot.getLongitude(), walkSpot.getLatitude(), radius));
            merged.addAll(fetchByType("TOILET", walkSpot.getLongitude(), walkSpot.getLatitude(), radius));

            return merged.stream()
                    .sorted(Comparator.comparingDouble(InfrastructureResponse::getDistance))
                    .limit(15)
                    .toList();
        }

        return fetchByType(type, walkSpot.getLongitude(), walkSpot.getLatitude(), radius);
    }

    private List<InfrastructureResponse> fetchByType(String type, double longitude, double latitude, int radius) {
        KakaoPlaceSearchResponse response;

        switch (type.toUpperCase()) {
            case "CAFE" -> response = kakaoLocalClient.searchByCategory("CE7", longitude, latitude, radius);
            case "CONVENIENCE_STORE" -> response = kakaoLocalClient.searchByCategory("CS2", longitude, latitude, radius);
            case "TOILET" -> response = kakaoLocalClient.searchByKeyword("화장실", longitude, latitude, radius);
            default -> throw new IllegalArgumentException("지원하지 않는 type 입니다. type=" + type);
        }

        if (response == null || response.documents() == null) {
            return List.of();
        }

        return response.documents().stream()
                .map(doc -> InfrastructureResponse.builder()
                        .name(doc.placeName())
                        .type(type.toUpperCase())
                        .latitude(parseDouble(doc.y()))
                        .longitude(parseDouble(doc.x()))
                        .distance(parseDouble(doc.distance()))
                        .build())
                .toList();
    }

    private double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    private double calculateDistanceMeter(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c * 1000;
    }
}