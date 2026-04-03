package com.example.sum_gil_be.walkspot.service;

import com.example.sum_gil_be.walkspot.domain.dto.CctvPoint;
import com.example.sum_gil_be.walkspot.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CctvScoreService {

    private static final double DEFAULT_RADIUS_METERS = 500.0;

    private final CctvCsvService cctvCsvService;

    public int countNearbyCameras(double lat, double lng) {
        return countNearbyCameras(lat, lng, DEFAULT_RADIUS_METERS);
    }

    public int countNearbyCameras(double lat, double lng, double radiusMeters) {
        int totalCameraCount = cctvCsvService.getSafetyCctvs().stream()
                .filter(cctv -> isWithinRadius(lat, lng, cctv, radiusMeters))
                .map(CctvPoint::cameraCount)
                .filter(count -> count != null && count > 0)
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("[CctvScoreService] lat = " + lat + ", lng = " + lng
                + ", radiusMeters = " + radiusMeters
                + ", totalCameraCount = " + totalCameraCount);

        return totalCameraCount;
    }

    private boolean isWithinRadius(double placeLat, double placeLng, CctvPoint cctv, double radiusMeters) {
        double distance = GeoUtils.distanceMeters(
                placeLat, placeLng,
                cctv.latitude(), cctv.longitude()
        );
        return distance <= radiusMeters;
    }

    public int normalizeCctvCount(int cctvCount) {
        if (cctvCount <= 2) return 20;
        if (cctvCount <= 5) return 40;
        if (cctvCount <= 10) return 60;
        if (cctvCount <= 20) return 80;
        return 100;
    }
}