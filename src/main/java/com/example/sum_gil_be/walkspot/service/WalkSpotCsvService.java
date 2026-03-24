package com.example.sum_gil_be.walkspot.service;

import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkSpotCsvService {

    private static final String WALKSPOT_CSV_PATH = "data/walkspot.csv";

    private final WalkSpotRepository walkSpotRepository;

    @PostConstruct
    public void loadCsv() {
        try {
            ClassPathResource resource = new ClassPathResource(WALKSPOT_CSV_PATH);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), Charset.forName("MS949")))) {

                String headerLine = br.readLine();
                if (headerLine == null) {
                    throw new IllegalStateException("산책로 CSV 헤더가 없습니다.");
                }

                System.out.println("[WalkSpotCsvService] header = " + headerLine);

                int savedCount = 0;
                String line;

                while ((line = br.readLine()) != null) {
                    List<String> tokens = parseCsvLine(line);

                    // 컬럼 수 체크
                    if (tokens.size() < 16) {
                        continue;
                    }

                    String name = getValue(tokens, 2);         // WLK_COURS_NM
                    String region = getValue(tokens, 4);       // SIGNGU_NM
                    String description = getValue(tokens, 8);  // ADIT_DC
                    String address = getValue(tokens, 13);     // LNM_ADDR
                    Double latitude = parseDouble(getValue(tokens, 14));   // COURS_SPOT_LA
                    Double longitude = parseDouble(getValue(tokens, 15));  // COURS_SPOT_LO

                    if (name == null || name.isBlank() || latitude == null || longitude == null) {
                        continue;
                    }

                    String normalizedAddress = (address == null || address.isBlank()) ? null : address.trim();

                    // 중복 저장 방지
                    if (walkSpotRepository.existsByNameAndAddress(name.trim(), normalizedAddress)) {
                        continue;
                    }

                    WalkSpot walkSpot = WalkSpot.of(
                            name.trim(),
                            normalizedAddress,
                            latitude,
                            longitude,
                            description,
                            region
                    );

                    walkSpotRepository.save(walkSpot);
                    savedCount++;
                }

                System.out.println("[WalkSpotCsvService] saved count = " + savedCount);
            }

        } catch (Exception e) {
            throw new RuntimeException("산책로 CSV 로딩 실패", e);
        }
    }

    private String getValue(List<String> tokens, int index) {
        if (index >= tokens.size()) {
            return null;
        }
        String value = tokens.get(index);
        return value == null ? null : value.trim();
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