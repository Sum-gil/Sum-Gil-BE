package com.example.sum_gil_be.auth.service;

import com.example.sum_gil_be.auth.domain.dto.AuthDto.KakaoUserInfo;
import com.example.sum_gil_be.auth.domain.dto.AuthDto.TokenResponse;
import com.example.sum_gil_be.config.jwt.JwtUtil;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.domain.repository.UserRepository;
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
        // 1. 카카오 사용자 정보 가져오기
        KakaoUserInfo kakaoUser = kakaoService.getUserInfo(code);

        // 2. 유저 정보 확인 및 저장 (회원가입/로그인)
        UserEntity user = userRepository.findByKakaoId(kakaoUser.getId())
                .orElseGet(() -> {
                    // 최초 로그인 시 회원가입 처리
                    UserEntity newUser = UserEntity.builder()
                            .kakaoId(kakaoUser.getId())
                            .email(kakaoUser.getEmail())
                            .nickname(kakaoUser.getNickname())
                            .interestRegion(null)
                            .build();
                    return userRepository.save(newUser);
                });

        // 3. JWT 토큰 발급 (원본 생성)
        String accessToken = jwtUtil.createAccessToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        // 4. Refresh Token SHA-256 해싱 후 DB 저장 
        String hashedToken = jwtUtil.hashToken(refreshToken);
        user.updateRefreshToken(hashedToken);

        // 5. 응답 시에는 해싱되지 않은 원본 토큰을 반환 
        return new TokenResponse(accessToken, refreshToken, user.getId());
    }

    @Transactional
    public void logout(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.invalidateRefreshToken();

    }
}