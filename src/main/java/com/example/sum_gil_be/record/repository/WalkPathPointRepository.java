package com.example.sum_gil_be.record.repository;

import com.example.sum_gil_be.record.domain.entity.WalkPathPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkPathPointRepository extends JpaRepository<WalkPathPoint, Long> {
    List<WalkPathPoint> findByWalkRecordIdOrderBySequenceAsc(Long walkRecordId);
}