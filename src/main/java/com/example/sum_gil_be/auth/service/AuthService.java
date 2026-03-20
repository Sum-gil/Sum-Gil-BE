package com.example.sum_gil_be.auth.service;

import com.example.sum_gil_be.auth.domain.dto.KakaoUserInfo;
import com.example.sum_gil_be.auth.domain.dto.TokenResponse;
import com.example.sum_gil_be.config.jwt.JwtUtil;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public TokenResponse socialLogin(String code) {
        KakaoUserInfo kakaoUser = kakaoService.getUserInfo(code);

        UserEntity user = userRepository.findByKakaoId(kakaoUser.getId())
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .kakaoId(kakaoUser.getId())
                            .email(kakaoUser.getEmail())
                            .nickname(kakaoUser.getNickname())
                            .interestRegion(null)
                            .build();
                    return userRepository.save(newUser);
                });

        String accessToken = jwtUtil.createAccessToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());
 
        String hashedToken = jwtUtil.hashToken(refreshToken);
        user.updateRefreshToken(hashedToken);

        return new TokenResponse(accessToken, refreshToken, user.getId());
    }

    @Transactional
    public void logout(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.invalidateRefreshToken();

    }
}