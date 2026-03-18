package com.example.sum_gil_be.dashboard.service;

import com.example.sum_gil_be.dashboard.client.SeoulPopulationClient;
import com.example.sum_gil_be.dashboard.domain.dto.PopulationInfo;
import com.example.sum_gil_be.dashboard.domain.dto.SeoulPopulationApiResponse;
import com.example.sum_gil_be.dashboard.domain.dto.SeoulPopulationRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SeoulPopulationService {

    private final SeoulPopulationClient seoulPopulationClient;

    private static final Map<String, String> GU_ROMANIZED = Map.ofEntries(
            Map.entry("강남", "Gangnam"),
            Map.entry("강동", "Gangdong"),
            Map.entry("강북", "Gangbuk"),
            Map.entry("강서", "Gangseo"),
            Map.entry("관악", "Gwanak"),
            Map.entry("광진", "Gwangjin"),
            Map.entry("구로", "Guro"),
            Map.entry("금천", "Geumcheon"),
            Map.entry("노원", "Nowon"),
            Map.entry("도봉", "Dobong"),
            Map.entry("동대문", "Dongdaemun"),
            Map.entry("동작", "Dongjak"),
            Map.entry("마포", "Mapo"),
            Map.entry("서대문", "Seodaemun"),
            Map.entry("서초", "Seocho"),
            Map.entry("성동", "Seongdong"),
            Map.entry("성북", "Seongbuk"),
            Map.entry("송파", "Songpa"),
            Map.entry("양천", "Yangcheon"),
            Map.entry("영등포", "Yeongdeungpo"),
            Map.entry("용산", "Yongsan"),
            Map.entry("은평", "Eunpyeong"),
            Map.entry("종로", "Jongno"),
            Map.entry("중", "Jung"),
            Map.entry("중랑", "Jungnang")
    );

    public PopulationInfo getPopulation(String guName, String dongName) {
        SeoulPopulationApiResponse response = seoulPopulationClient.fetchPopulation();

        System.out.println("[SeoulPopulationService] guName = " + guName);
        System.out.println("[SeoulPopulationService] dongName = " + dongName);

        if (response == null || response.getWrapper() == null || response.getWrapper().getRow() == null) {
            System.out.println("[SeoulPopulationService] response or row is null");
            return new PopulationInfo(dongName, null, null);
        }

        List<SeoulPopulationRow> rows = response.getWrapper().getRow();

        if (rows.isEmpty()) {
            System.out.println("[SeoulPopulationService] rows is empty");
            return new PopulationInfo(dongName, null, null);
        }

        rows.stream()
                .limit(20)
                .forEach(row -> System.out.println(
                        "[SeoulPopulationService] row gu = " + row.getAutonomousDistrict()
                                + ", row dong = " + row.getAdministrativeDistrict()
                                + ", visitorCount = " + row.getVisitorCount()
                                + ", sensingTime = " + row.getSensingTime()
                ));

        List<SeoulPopulationRow> guMatched = rows.stream()
                .filter(row -> matchesGu(row.getAutonomousDistrict(), guName))
                .toList();

        System.out.println("[SeoulPopulationService] guMatched size = " + guMatched.size());

        if (guMatched.isEmpty()) {
            System.out.println("[SeoulPopulationService] no matched gu row");
            return new PopulationInfo(dongName, null, null);
        }

        // 아직 서울 API가 영문 동 이름을 주기 때문에,
        // exact dong 매칭은 추후 한글↔영문 동 이름 매핑이 필요함.
        // 현재는 같은 구 안에서 최신 row 기반 평균값 사용.
        int avgVisitorCount = (int) Math.round(
                guMatched.stream()
                        .map(SeoulPopulationRow::getVisitorCount)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0)
        );

        String latestSensingTime = guMatched.stream()
                .map(SeoulPopulationRow::getSensingTime)
                .filter(Objects::nonNull)
                .max(String::compareTo)
                .orElse(null);

        System.out.println("[SeoulPopulationService] avgVisitorCount = " + avgVisitorCount);
        System.out.println("[SeoulPopulationService] latestSensingTime = " + latestSensingTime);

        return new PopulationInfo(
                dongName + " (구 평균)",
                avgVisitorCount,
                latestSensingTime
        );
    }

    private boolean matchesGu(String apiAutonomousDistrict, String guName) {
        if (apiAutonomousDistrict == null || guName == null) {
            return false;
        }

        String romanizedGu = GU_ROMANIZED.get(guName);
        if (romanizedGu == null) {
            return false;
        }

        String normalizedApiGu = normalizeEnglishGu(apiAutonomousDistrict);
        String normalizedTargetGu = normalizeEnglishGu(romanizedGu);

        return normalizedApiGu.equalsIgnoreCase(normalizedTargetGu);
    }

    private String normalizeEnglishGu(String value) {
        if (value == null) return "";
        return value.replace("-gu", "")
                .replace("gu", "")
                .replaceAll("[^a-zA-Z]", "")
                .toLowerCase()
                .trim();
    }
}