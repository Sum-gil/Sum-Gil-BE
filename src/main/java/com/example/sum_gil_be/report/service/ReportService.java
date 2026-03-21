package com.example.sum_gil_be.report.service;

import com.example.sum_gil_be.record.domain.entity.WalkRecord;
import com.example.sum_gil_be.record.repository.WalkRecordRepository;
import com.example.sum_gil_be.report.domain.dto.MonthlyReportResponse;
import com.example.sum_gil_be.report.domain.dto.WalkReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final WalkRecordRepository walkRecordRepository;

    public WalkReportResponse getWalkReport(String userId, Long walkRecordId) {
        Long loginUserId = parseUserId(userId);

        WalkRecord walkRecord = walkRecordRepository.findByIdAndUserId(walkRecordId, loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책 기록을 찾을 수 없습니다."));

        int totalDurationSeconds = getDurationSeconds(walkRecord);
        double totalDistance = nvl(walkRecord.getTotalDistance());
        double averageSpeedKmh = calculateAverageSpeed(totalDistance, totalDurationSeconds);
        double calories = nvl(walkRecord.getCalories());
        int averageHealthScore = nvl(walkRecord.getAverageHealthScore());

        return WalkReportResponse.builder()
                .walkRecordId(walkRecord.getId())
                .walkSpotId(walkRecord.getWalkSpotId())
                .startedAt(walkRecord.getStartedAt())
                .endedAt(walkRecord.getEndedAt())
                .totalDurationSeconds(totalDurationSeconds)
                .totalDurationText(formatDuration(totalDurationSeconds))
                .totalDistanceKm(round2(totalDistance))
                .averageSpeedKmh(round2(averageSpeedKmh))
                .calories(round2(calories))
                .averageHealthScore(averageHealthScore)
                .reportMessage(buildReportMessage(totalDistance, totalDurationSeconds, averageHealthScore))
                .build();
    }

    public MonthlyReportResponse getMonthlyReport(String userId, Integer year, Integer month) {
        Long loginUserId = parseUserId(userId);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        List<WalkRecord> records =
                walkRecordRepository.findAllByUserIdAndStartedAtBetween(loginUserId, start, end);

        int totalWalkCount = records.size();

        double totalDistance = records.stream()
                .mapToDouble(record -> nvl(record.getTotalDistance()))
                .sum();

        int totalDurationSeconds = records.stream()
                .mapToInt(this::getDurationSeconds)
                .sum();

        int averageDurationSeconds = totalWalkCount == 0 ? 0 : totalDurationSeconds / totalWalkCount;

        double averageHealthScore = totalWalkCount == 0 ? 0.0
                : records.stream()
                        .mapToInt(record -> nvl(record.getAverageHealthScore()))
                        .average()
                        .orElse(0.0);

        double totalCalories = records.stream()
                .mapToDouble(record -> nvl(record.getCalories()))
                .sum();

        return MonthlyReportResponse.builder()
                .year(year)
                .month(month)
                .totalWalkCount(totalWalkCount)
                .totalDistanceKm(round2(totalDistance))
                .totalDurationSeconds(totalDurationSeconds)
                .totalDurationText(formatDuration(totalDurationSeconds))
                .averageDurationSeconds(averageDurationSeconds)
                .averageDurationText(formatDuration(averageDurationSeconds))
                .averageHealthScore(round2(averageHealthScore))
                .totalCalories(round2(totalCalories))
                .build();
    }

    private Long parseUserId(String userId) {
        return Long.parseLong(userId);
    }

    private int getDurationSeconds(WalkRecord walkRecord) {
        if (walkRecord.getDurationSeconds() != null) {
            return walkRecord.getDurationSeconds();
        }
        if (walkRecord.getStartedAt() != null && walkRecord.getEndedAt() != null) {
            return (int) Duration.between(walkRecord.getStartedAt(), walkRecord.getEndedAt()).getSeconds();
        }
        return 0;
    }

    private double calculateAverageSpeed(double totalDistance, int totalDurationSeconds) {
        if (totalDurationSeconds <= 0) return 0.0;
        return totalDistance / (totalDurationSeconds / 3600.0);
    }

    private String buildReportMessage(double distance, int durationSeconds, int healthScore) {
        if (distance >= 5.0) return "오늘은 꽤 긴 산책을 했어요. 정말 잘 걸었어요!";
        if (healthScore >= 80) return "건강 점수가 높은 산책이었어요.";
        if (durationSeconds >= 3600) return "한 시간 이상 꾸준히 걸었어요. 좋은 습관이에요!";
        return "오늘도 한 걸음씩 건강을 쌓았어요.";
    }

    private String formatDuration(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) return hours + "시간 " + minutes + "분";
        if (minutes > 0) return minutes + "분 " + seconds + "초";
        return seconds + "초";
    }

    private double nvl(Double value) {
        return value == null ? 0.0 : value;
    }

    private int nvl(Integer value) {
        return value == null ? 0 : value;
    }

    private double round2(double value) {
        return Math.round(value * 100) / 100.0;
    }
}