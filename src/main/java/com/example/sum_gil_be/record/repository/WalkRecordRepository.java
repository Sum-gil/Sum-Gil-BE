package com.example.sum_gil_be.record.repository;

import com.example.sum_gil_be.record.domain.entity.WalkRecord;
import com.example.sum_gil_be.record.domain.entity.WalkRecordStatus;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {
    List<WalkRecord> findByUserIdOrderByStartedAtDesc(Long userId);
    Optional<WalkRecord> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndStatus(Long userId, WalkRecordStatus status);
        @EntityGraph(attributePaths = {"walkSpot"})
    Optional<WalkRecord> findByIdAndUser_KakaoId(Long id, String kakaoId);

    @EntityGraph(attributePaths = {"walkSpot"})
    List<WalkRecord> findAllByUser_KakaoIdAndStartedAtBetween(
            String kakaoId,
            LocalDateTime start,
            LocalDateTime end
    );
}