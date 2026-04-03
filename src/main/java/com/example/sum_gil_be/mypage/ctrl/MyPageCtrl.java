package com.example.sum_gil_be.mypage.ctrl;

import com.example.sum_gil_be.mypage.domain.dto.MyPageSummaryResponse;
import com.example.sum_gil_be.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "MyPage API", description = "마이페이지 관련 API")
public class MyPageCtrl {

    private final MyPageService myPageService;

    @Operation(
            summary = "마이페이지 요약 조회",
            description = "즐겨찾기 수, 총 산책 횟수, 총 산책 거리, 총 소모 칼로리, 평균 건강 점수, 총 산책 시간을 조회한다."
    )
    @GetMapping("/summary")
    public ResponseEntity<MyPageSummaryResponse> getSummary(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(myPageService.getSummary(userDetails.getUsername()));
    }
}