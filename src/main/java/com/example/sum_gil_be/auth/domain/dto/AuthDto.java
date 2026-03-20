package com.example.sum_gil_be.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private Long userId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialLoginRequest {
        private String code;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoUserInfo {
        private String id;
        private String email;
        private String nickname;
    }
}
