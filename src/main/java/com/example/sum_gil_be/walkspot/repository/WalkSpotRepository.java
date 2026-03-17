package com.example.sum_gil_be.walkspot.repository;

import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkSpotRepository extends JpaRepository<WalkSpot, Long> {

    List<WalkSpot> findByRegion(String region);

    List<WalkSpot> findByRegionContaining(String region);
}