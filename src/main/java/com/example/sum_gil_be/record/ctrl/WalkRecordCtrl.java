package com.example.sum_gil_be.record.ctrl;

import com.example.sum_gil_be.record.domain.dto.WalkEndRequest;
import com.example.sum_gil_be.record.domain.dto.WalkPointRequest;
import com.example.sum_gil_be.record.domain.dto.WalkRecordDetailResponse;
import com.example.sum_gil_be.record.domain.dto.WalkRecordListResponse;
import com.example.sum_gil_be.record.domain.dto.WalkStartRequest;
import com.example.sum_gil_be.record.domain.dto.WalkStartResponse;
import com.example.sum_gil_be.record.service.WalkRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/walk-records")
@RequiredArgsConstructor
public class WalkRecordCtrl {

    private final WalkRecordService walkRecordService;

    @PostMapping("/start")
    public ResponseEntity<WalkStartResponse> startWalk(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody WalkStartRequest request
    ) {
        return ResponseEntity.ok(walkRecordService.startWalk(userDetails.getUsername(), request));
    }

    @PostMapping("/{walkRecordId}/points")
    public ResponseEntity<Void> savePoints(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId,
            @RequestBody WalkPointRequest request
    ) {
        walkRecordService.savePoints(userDetails.getUsername(), walkRecordId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{walkRecordId}/end")
    public ResponseEntity<Void> endWalk(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId,
            @RequestBody WalkEndRequest request
    ) {
        walkRecordService.endWalk(userDetails.getUsername(), walkRecordId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WalkRecordListResponse>> getMyWalkRecords(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(walkRecordService.getMyWalkRecords(userDetails.getUsername()));
    }

    @GetMapping("/{walkRecordId}")
    public ResponseEntity<WalkRecordDetailResponse> getWalkRecordDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long walkRecordId
    ) {
        return ResponseEntity.ok(walkRecordService.getWalkRecordDetail(userDetails.getUsername(), walkRecordId));
    }
}