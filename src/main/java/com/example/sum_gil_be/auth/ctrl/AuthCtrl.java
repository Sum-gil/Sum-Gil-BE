package com.example.sum_gil_be.auth.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sum_gil_be.auth.service.KakaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthCtrl {

    private final KakaoService kakaoService;

    @GetMapping("/api/auth/oauth/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 전달받아 엑세스 토큰을 반환합니다.")
    public ResponseEntity<String> socialLogin(@RequestParam("code") String code) {

        System.out.println(" 수신된 인가 코드: " + code);
        
        String tokenResponse = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
