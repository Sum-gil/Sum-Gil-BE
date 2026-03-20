package com.example.sum_gil_be.record.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "walk_path_points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkPathPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "walk_record_id")
    private WalkRecord walkRecord;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public WalkPathPoint(WalkRecord walkRecord, Double latitude, Double longitude, Integer sequence, LocalDateTime recordedAt) {
        this.walkRecord = walkRecord;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sequence = sequence;
        this.recordedAt = recordedAt;
    }
}