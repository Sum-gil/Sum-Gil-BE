package com.example.sum_gil_be.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    //  설정파일에 적어둔 Client Secret 값을 가져옵니다.
    @Value("${kakao.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        // 1. HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 2. HTTP Body 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        
        //  Client Secret이 활성화 되어있으므로 반드시 추가해야 함
        params.add("client_secret", clientSecret);

        // 3. HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        
        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            log.info("카카오 토큰 발급 성공!");
            return response.getBody(); 
            
        } catch (Exception e) {
            log.error("카카오 토큰 발급 실패: {}", e.getMessage());
            return "토큰 요청 실패: " + e.getMessage();
        }
    }
}