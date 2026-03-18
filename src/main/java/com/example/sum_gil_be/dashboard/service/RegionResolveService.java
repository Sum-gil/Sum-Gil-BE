package com.example.sum_gil_be.dashboard.service;

import com.example.sum_gil_be.dashboard.client.KakaoLocalClient;
import com.example.sum_gil_be.dashboard.domain.dto.ResolvedRegion;
import com.example.sum_gil_be.dashboard.domain.dto.kakao.KakaoCoord2RegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionResolveService {

    private final KakaoLocalClient kakaoLocalClient;

    public ResolvedRegion resolve(double lat, double lng) {
        KakaoCoord2RegionResponse response = kakaoLocalClient.coordToRegion(lat, lng);

        if (response == null || response.documents() == null || response.documents().isEmpty()) {
            throw new IllegalArgumentException("해당 좌표에 대한 지역 정보를 찾을 수 없습니다.");
        }

        KakaoCoord2RegionResponse.Document selected = response.documents().stream()
                .filter(doc -> "H".equals(doc.region_type()))
                .findFirst()
                .orElse(response.documents().get(0));

        String sidoName = normalizeSido(selected.region_1depth_name());
        String guName = extractGuName(selected.region_2depth_name());
        String dongName = extractDongName(selected.region_3depth_name());
        String fullAddress = selected.address_name();

        return new ResolvedRegion(sidoName, guName, dongName, fullAddress);
    }

    private String normalizeSido(String raw) {
        if (raw == null || raw.isBlank()) {
            return "기타";
        }

        return raw
                .replace("특별시", "")
                .replace("광역시", "")
                .replace("특별자치시", "")
                .replace("특별자치도", "")
                .replace("자치시", "")
                .replace("자치도", "")
                .trim();
    }

    private String extractGuName(String raw) {
        if (raw == null || raw.isBlank()) {
            return "기타";
        }

        String first = raw.split(" ")[0];

        return first
                .replace("시", "")
                .replace("군", "")
                .replace("구", "")
                .trim();
    }

    private String extractDongName(String raw) {
        if (raw == null || raw.isBlank()) {
            return "기타";
        }

        return raw.replaceAll("\\s+", "").trim();
    }
}