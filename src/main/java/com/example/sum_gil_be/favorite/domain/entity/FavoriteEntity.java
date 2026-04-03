package com.example.sum_gil_be.favorite.domain.entity;

import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favoritespot") 
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkspot_id", nullable = false)
    private WalkSpot walkSpot;

    @Builder
    public FavoriteEntity(UserEntity user, WalkSpot walkSpot) {
        this.user = user;
        this.walkSpot = walkSpot;
    }
}