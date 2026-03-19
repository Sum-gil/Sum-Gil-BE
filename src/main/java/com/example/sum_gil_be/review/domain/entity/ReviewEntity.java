package com.example.sum_gil_be.review.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long rating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkspot_id")
    private WalkSpot walkSpot; 

    @Builder
    public ReviewEntity(Long rating, String content, UserEntity user, WalkSpot walkSpot) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.walkSpot = walkSpot;
    }

    public void updateReview(Long rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}