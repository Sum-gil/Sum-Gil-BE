package com.example.sum_gil_be.report.ctrl;

import com.example.sum_gil_be.report.domain.dto.MonthlyReportResponse;
import com.example.sum_gil_be.report.domain.dto.WalkReportResponse;
import com.example.sum_gil_be.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report API", description = "산책 리포트 조회 API")
public class ReportCtrl {

    private final ReportService reportService;

    @GetMapping("/walks/{walkRecordId}")
    @Operation(summary = "특정 산책 리포트 조회", description = "산책 기록 1건에 대한 상세 리포트를 조회합니다.")
    public ResponseEntity<WalkReportResponse> getWalkReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId
    ) {
        return ResponseEntity.ok(
                reportService.getWalkReport(userDetails.getUsername(), walkRecordId)
        );
    }

    @GetMapping("/monthly")
    @Operation(summary = "월별 산책 통계 조회", description = "특정 연/월 기준 산책 거리, 시간, 평균 건강 점수를 조회합니다.")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return ResponseEntity.ok(
                reportService.getMonthlyReport(userDetails.getUsername(), year, month)
        );
    }
}