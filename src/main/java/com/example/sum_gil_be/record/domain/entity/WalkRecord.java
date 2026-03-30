package com.example.sum_gil_be.record.domain.entity;

import com.example.sum_gil_be.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "walk_records")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private Long walkSpotId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column
    private Double totalDistance;

    @Column
    private Integer durationSeconds;

    @Column
    private Double calories;

    @Column
    private Integer averageHealthScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalkRecordStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private Boolean notified10m = false;

    @Column
    private Boolean notified20m = false;

    @Column
    private Boolean notified30m = false;

    @Column
    private Boolean healthWarningSent = false;

    @Builder
    public WalkRecord(UserEntity user, Long walkSpotId, LocalDateTime startedAt, WalkRecordStatus status) {
        this.user = user;
        this.walkSpotId = walkSpotId;
        this.startedAt = startedAt;
        this.status = status;
    }

    public void complete(LocalDateTime endedAt, Double totalDistance, Integer durationSeconds, Double calories, Integer averageHealthScore) {
        this.endedAt = endedAt;
        this.totalDistance = totalDistance;
        this.durationSeconds = durationSeconds;
        this.calories = calories;
        this.averageHealthScore = averageHealthScore;
        this.status = WalkRecordStatus.COMPLETED;
    }

    public void mark10mNotified() {
        this.notified10m = true;
    }

    public void mark20mNotified() {
        this.notified20m = true;
    }

    public void mark30mNotified() {
        this.notified30m = true;
    }

    public void markHealthWarningSent() {
        this.healthWarningSent = true;
    }
}