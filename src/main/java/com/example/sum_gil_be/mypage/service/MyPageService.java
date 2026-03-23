package com.example.sum_gil_be.mypage.service;

import com.example.sum_gil_be.favorite.repository.FavoriteRepository;
import com.example.sum_gil_be.mypage.domain.dto.MyPageSummaryResponse;
import com.example.sum_gil_be.record.repository.WalkRecordRepository;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final WalkRecordRepository walkRecordRepository;

    public MyPageSummaryResponse getSummary(String userIdString) {
        Long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        long favoriteCount = favoriteRepository.countByUserId(user.getId());
        long totalWalkCount = walkRecordRepository.countByUserId(user.getId());

        Double totalDistanceMeters = walkRecordRepository.sumTotalDistanceByUserId(user.getId());
        Double totalCalories = walkRecordRepository.sumCaloriesByUserId(user.getId());
        Double averageHealthScore = walkRecordRepository.avgHealthScoreByUserId(user.getId());
        Long totalDurationSeconds = walkRecordRepository.sumDurationSecondsByUserId(user.getId());

        double safeDistanceMeters = totalDistanceMeters != null ? totalDistanceMeters : 0.0;
        double safeCalories = totalCalories != null ? totalCalories : 0.0;
        double safeAverageHealthScore = averageHealthScore != null ? averageHealthScore : 0.0;
        long safeTotalDurationSeconds = totalDurationSeconds != null ? totalDurationSeconds : 0L;

        return MyPageSummaryResponse.builder()
                .favoriteCount(favoriteCount)
                .totalWalkCount(totalWalkCount)
                .totalDistanceKm(roundTo2(safeDistanceMeters / 1000.0))
                .totalCalories(roundTo1(safeCalories))
                .averageHealthScore(roundTo1(safeAverageHealthScore))
                .totalDurationSeconds(safeTotalDurationSeconds)
                .totalDurationText(formatDuration(safeTotalDurationSeconds))
                .build();
    }

    private double roundTo2(double value) {
        return Math.round(value * 100) / 100.0;
    }

    private double roundTo1(double value) {
        return Math.round(value * 10) / 10.0;
    }

    private String formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return hours + "시간 " + minutes + "분 " + seconds + "초";
        }
        if (minutes > 0) {
            return minutes + "분 " + seconds + "초";
        }
        return seconds + "초";
    }
}