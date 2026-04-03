package com.example.sum_gil_be.record.ctrl;

import com.example.sum_gil_be.record.domain.dto.WalkEndRequest;
import com.example.sum_gil_be.record.domain.dto.WalkPointRequest;
import com.example.sum_gil_be.record.domain.dto.WalkRecordDetailResponse;
import com.example.sum_gil_be.record.domain.dto.WalkRecordListResponse;
import com.example.sum_gil_be.record.domain.dto.WalkStartRequest;
import com.example.sum_gil_be.record.domain.dto.WalkStartResponse;
import com.example.sum_gil_be.record.service.WalkRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WalkRecord", description = "산책 기록 API")
@RestController
@RequestMapping("/api/walk-records")
@RequiredArgsConstructor
public class WalkRecordCtrl {

    private final WalkRecordService walkRecordService;

    @Operation(summary = "산책 시작", description = "산책 시작 시간을 기록한다.")
    @PostMapping("/start")
    public ResponseEntity<WalkStartResponse> startWalk(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody WalkStartRequest request
    ) {
        return ResponseEntity.ok(walkRecordService.startWalk(userDetails.getUsername(), request));
    }

    @Operation(summary = "산책 경로 저장", description = "산책 중 GPS 좌표를 저장한다.")
    @PostMapping("/{walkRecordId}/points")
    public ResponseEntity<Void> savePoints(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId,
            @RequestBody WalkPointRequest request
    ) {
        walkRecordService.savePoints(userDetails.getUsername(), walkRecordId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "산책 종료", description = "산책 종료 시 거리, 시간, 칼로리 등을 저장한다.")
    @PostMapping("/{walkRecordId}/end")
    public ResponseEntity<Void> endWalk(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId,
            @RequestBody WalkEndRequest request
    ) {
        walkRecordService.endWalk(userDetails.getUsername(), walkRecordId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 산책 기록 목록 조회", description = "사용자의 전체 산책 기록 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<List<WalkRecordListResponse>> getMyWalkRecords(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(walkRecordService.getMyWalkRecords(userDetails.getUsername()));
    }
    @Operation(summary = "산책 기록 상세 조회", description = "특정 산책 기록과 경로를 조회한다.")
    @GetMapping("/{walkRecordId}")
    public ResponseEntity<WalkRecordDetailResponse> getWalkRecordDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId
    ) {
        return ResponseEntity.ok(walkRecordService.getWalkRecordDetail(userDetails.getUsername(), walkRecordId));
    }
}