package com.example.sum_gil_be.walkspot.service;

import com.example.sum_gil_be.walkspot.domain.dto.CctvPoint;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class CctvCsvService {

    private static final String CCTV_CSV_PATH = "data/seoul_cctv.csv";
    private static final String PURPOSE_SAFE_LIFE = "생활방범";

    private final List<CctvPoint> cctvPoints = new ArrayList<>();

    @PostConstruct
    public void loadCsv() {
        try {
            ClassPathResource resource = new ClassPathResource(CCTV_CSV_PATH);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), Charset.forName("MS949")))) {

                String headerLine = br.readLine();
                if (headerLine == null) {
                    throw new IllegalStateException("CCTV CSV 헤더가 없습니다.");
                }

                System.out.println("[CctvCsvService] header = " + headerLine);

                String line;
                while ((line = br.readLine()) != null) {
                    List<String> tokens = parseCsvLine(line);

                    if (tokens.size() < 15) {
                        continue;
                    }

                    String purpose = getValue(tokens, 5);      // 설치목적구분
                    Integer cameraCount = parseInteger(getValue(tokens, 6)); // 카메라대수
                    Double latitude = parseDouble(getValue(tokens, 12));     // WGS84위도
                    Double longitude = parseDouble(getValue(tokens, 13));    // WGS84경도

                    if (purpose == null || latitude == null || longitude == null) {
                        continue;
                    }

                    cctvPoints.add(new CctvPoint(
                            purpose.trim(),
                            cameraCount == null ? 0 : cameraCount,
                            latitude,
                            longitude
                    ));
                }
            }

            System.out.println("[CctvCsvService] loaded cctvPoints size = " + cctvPoints.size());
            System.out.println("[CctvCsvService] safety cctv size = " + getSafetyCctvs().size());

        } catch (Exception e) {
            throw new RuntimeException("CCTV CSV 로딩 실패", e);
        }
    }

    public List<CctvPoint> getAll() {
        return cctvPoints;
    }

    public List<CctvPoint> getSafetyCctvs() {
        return cctvPoints.stream()
                .filter(c -> PURPOSE_SAFE_LIFE.equals(c.purpose()))
                .toList();
    }

    private String getValue(List<String> tokens, int index) {
        if (index >= tokens.size()) {
            return null;
        }
        String value = tokens.get(index);
        return value == null ? null : value.trim();
    }

    private Integer parseInteger(String value) {
        try {
            if (value == null || value.isBlank()) {
                return null;
            }
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            if (value == null || value.isBlank()) {
                return null;
            }
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null) {
            return result;
        }

        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }

        result.add(sb.toString());
        return result;
    }
}