package com.example.sum_gil_be.walkspot.repository;

import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkSpotRepository extends JpaRepository<WalkSpot, Long> {
}