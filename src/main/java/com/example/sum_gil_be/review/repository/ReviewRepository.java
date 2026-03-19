package com.example.sum_gil_be.review.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.sum_gil_be.review.domain.entity.ReviewEntity;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    
    List<ReviewEntity> findAllByWalkSpotIdOrderByCreatedAtDesc(Long walkSpotId);

    @Query("SELECT r.walkSpot FROM ReviewEntity r " +
           "GROUP BY r.walkSpot " +
           "ORDER BY COUNT(r) DESC")
    List<WalkSpot> findPopularPlaces(Pageable pageable);
}