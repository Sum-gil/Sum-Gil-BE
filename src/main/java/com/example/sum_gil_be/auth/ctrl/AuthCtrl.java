package com.example.sum_gil_be.auth.ctrl;

import com.example.sum_gil_be.auth.domain.dto.AuthDto.SocialLoginRequest;
import com.example.sum_gil_be.auth.domain.dto.AuthDto.TokenResponse;
import com.example.sum_gil_be.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthCtrl {

    private final AuthService authService;

    @PostMapping("/social-login")
    @Operation(summary = "카카오 로그인 POST API", description = "카카오 인가 코드를 JSON으로 전달받아 엑세스 토큰 및 내부 JWT를 반환합니다.")
    public ResponseEntity<TokenResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        TokenResponse tokenResponse = authService.socialLogin(request.getCode());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 사용자의 리프레시 토큰을 무효화합니다.")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("인증 정보가 없습니다.");
        }
        Long userId = Long.parseLong(userDetails.getUsername());
        authService.logout(userId);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
