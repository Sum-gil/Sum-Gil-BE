package com.example.sum_gil_be.record.service;

import com.example.sum_gil_be.record.domain.dto.*;
import com.example.sum_gil_be.record.domain.entity.WalkPathPoint;
import com.example.sum_gil_be.record.domain.entity.WalkRecord;
import com.example.sum_gil_be.record.domain.entity.WalkRecordStatus;
import com.example.sum_gil_be.record.repository.WalkPathPointRepository;
import com.example.sum_gil_be.record.repository.WalkRecordRepository;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkRecordService {

    private final WalkRecordRepository walkRecordRepository;
    private final WalkPathPointRepository walkPathPointRepository;
    private final UserRepository userRepository;

    @Transactional
    public WalkStartResponse startWalk(Long userId, WalkStartRequest request) {
        if (walkRecordRepository.existsByUserIdAndStatus(userId, WalkRecordStatus.IN_PROGRESS)) {
            throw new IllegalStateException("진행 중인 산책 기록이 이미 존재합니다.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        WalkRecord walkRecord = WalkRecord.builder()
                .user(user)
                .walkSpotId(request.walkSpotId())
                .startedAt(LocalDateTime.now())
                .status(WalkRecordStatus.IN_PROGRESS)
                .build();

        WalkRecord saved = walkRecordRepository.save(walkRecord);

        return new WalkStartResponse(
                saved.getId(),
                saved.getStartedAt(),
                saved.getStatus().name()
        );
    }

    @Transactional
    public void savePoints(Long userId, Long walkRecordId, WalkPointRequest request) {
        WalkRecord walkRecord = walkRecordRepository.findByIdAndUserId(walkRecordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("산책 기록을 찾을 수 없습니다."));

        if (walkRecord.getStatus() != WalkRecordStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행 중인 산책에만 좌표를 저장할 수 있습니다.");
        }

        if (request == null || request.points() == null || request.points().isEmpty()) {
            return;
        }

        List<WalkPathPoint> points = request.points().stream()
                .map(point -> WalkPathPoint.builder()
                        .walkRecord(walkRecord)
                        .latitude(point.latitude())
                        .longitude(point.longitude())
                        .sequence(point.sequence())
                        .recordedAt(point.recordedAt() != null ? point.recordedAt() : LocalDateTime.now())
                        .build())
                .toList();

        walkPathPointRepository.saveAll(points);
    }

    @Transactional
    public void endWalk(Long userId, Long walkRecordId, WalkEndRequest request) {
        WalkRecord walkRecord = walkRecordRepository.findByIdAndUserId(walkRecordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("산책 기록을 찾을 수 없습니다."));

        if (walkRecord.getStatus() != WalkRecordStatus.IN_PROGRESS) {
            throw new IllegalStateException("이미 종료된 산책입니다.");
        }

        walkRecord.complete(
                LocalDateTime.now(),
                request.totalDistance(),
                request.durationSeconds(),
                request.calories(),
                request.averageHealthScore()
        );
    }

    @Transactional(readOnly = true)
    public List<WalkRecordListResponse> getMyWalkRecords(Long userId) {
        return walkRecordRepository.findByUserIdOrderByStartedAtDesc(userId).stream()
                .map(record -> new WalkRecordListResponse(
                        record.getId(),
                        record.getStartedAt(),
                        record.getEndedAt(),
                        record.getTotalDistance(),
                        record.getDurationSeconds(),
                        record.getCalories(),
                        record.getAverageHealthScore(),
                        record.getStatus().name()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public WalkRecordDetailResponse getWalkRecordDetail(Long userId, Long walkRecordId) {
        WalkRecord record = walkRecordRepository.findByIdAndUserId(walkRecordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("산책 기록을 찾을 수 없습니다."));

        List<WalkPathPointResponse> pathPoints = walkPathPointRepository
                .findByWalkRecordIdOrderBySequenceAsc(walkRecordId)
                .stream()
                .map(point -> new WalkPathPointResponse(
                        point.getLatitude(),
                        point.getLongitude(),
                        point.getSequence(),
                        point.getRecordedAt()
                ))
                .toList();

        return new WalkRecordDetailResponse(
                record.getId(),
                record.getWalkSpotId(),
                record.getStartedAt(),
                record.getEndedAt(),
                record.getTotalDistance(),
                record.getDurationSeconds(),
                record.getCalories(),
                record.getAverageHealthScore(),
                record.getStatus().name(),
                pathPoints
        );
    }
}