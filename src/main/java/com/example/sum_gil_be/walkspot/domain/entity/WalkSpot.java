package com.example.sum_gil_be.walkspot.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "walkspot")
public class WalkSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "safety_score")
    private Integer safetyScore;

    @Column(length = 100)
    private String region;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "green_ratio")
    private Integer greenRatio;

    @Column(name = "night_safe")
    private Boolean nightSafe;

    private WalkSpot(
            String name,
            String address,
            Double latitude,
            Double longitude,
            String description,
            Integer safetyScore,
            String region,
            LocalDateTime createdAt,
            Integer greenRatio,
            Boolean nightSafe
    ) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.safetyScore = safetyScore;
        this.region = region;
        this.createdAt = createdAt;
        this.greenRatio = greenRatio;
        this.nightSafe = nightSafe;
    }

    public static WalkSpot of(
            String name,
            String address,
            Double latitude,
            Double longitude,
            String description,
            String region
    ) {
        return new WalkSpot(
                name,
                address,
                latitude,
                longitude,
                description,
                null,
                region,
                LocalDateTime.now(),
                null,
                null
        );
    }
}