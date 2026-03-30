package com.example.sum_gil_be.user.domain.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kakaoId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String interestRegion;

    @Column
    private String healthInfo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @Column(length = 500)
    private String refreshToken;

    @Column(length = 500)
    private String fcmToken;

    @Builder
    public UserEntity(String kakaoId, String email, String nickname, String interestRegion, String healthInfo,
            String refreshToken) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.interestRegion = interestRegion;
        this.healthInfo = healthInfo;
        this.refreshToken = refreshToken;
    }

    public boolean isRegistrationIncomplete() {
        return this.interestRegion == null || this.interestRegion.trim().isEmpty();
    }

    public void updateProfile(String nickname, String interestRegion) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.nickname = nickname;
        }
        if (interestRegion != null && !interestRegion.trim().isEmpty()) {
            this.interestRegion = interestRegion;
        }
    }

    public void updateRefreshToken(String hashedRefreshToken) {
        this.refreshToken = hashedRefreshToken;
    }

    public void invalidateRefreshToken() {
        this.refreshToken = null;
    }

    public void updateFcmToken(String token) {
        this.fcmToken = token;
    }
}