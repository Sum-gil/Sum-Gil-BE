package com.example.sum_gil_be.record.repository;

import com.example.sum_gil_be.record.domain.entity.WalkRecord;
import com.example.sum_gil_be.record.domain.entity.WalkRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

    List<WalkRecord> findByUserIdOrderByStartedAtDesc(Long userId);

    Optional<WalkRecord> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndStatus(Long userId, WalkRecordStatus status);

    List<WalkRecord> findAllByUserIdAndStartedAtBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );
    long countByUserId(Long userId);

    @Query("select coalesce(sum(w.totalDistance), 0) from WalkRecord w where w.user.id = :userId")
    Double sumTotalDistanceByUserId(@Param("userId") Long userId);

    @Query("select coalesce(sum(w.calories), 0) from WalkRecord w where w.user.id = :userId")
    Double sumCaloriesByUserId(@Param("userId") Long userId);

    @Query("select coalesce(avg(w.averageHealthScore), 0) from WalkRecord w where w.user.id = :userId")
    Double avgHealthScoreByUserId(@Param("userId") Long userId);

    @Query("select coalesce(sum(w.durationSeconds), 0) from WalkRecord w where w.user.id = :userId")
    Long sumDurationSecondsByUserId(@Param("userId") Long userId);
}