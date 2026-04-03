package com.example.sum_gil_be.record.service;

import com.example.sum_gil_be.notification.service.FcmService;
import com.example.sum_gil_be.record.domain.dto.WalkEndRequest;
import com.example.sum_gil_be.record.domain.dto.WalkPathPointResponse;
import com.example.sum_gil_be.record.domain.dto.WalkPointRequest;
import com.example.sum_gil_be.record.domain.dto.WalkRecordDetailResponse;
import com.example.sum_gil_be.record.domain.dto.WalkRecordListResponse;
import com.example.sum_gil_be.record.domain.dto.WalkStartRequest;
import com.example.sum_gil_be.record.domain.dto.WalkStartResponse;
import com.example.sum_gil_be.record.domain.entity.WalkPathPoint;
import com.example.sum_gil_be.record.domain.entity.WalkRecord;
import com.example.sum_gil_be.record.domain.entity.WalkRecordStatus;
import com.example.sum_gil_be.record.repository.WalkPathPointRepository;
import com.example.sum_gil_be.record.repository.WalkRecordRepository;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sum_gil_be.notification.service.FcmService;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkRecordService {

    private final WalkRecordRepository walkRecordRepository;
    private final WalkPathPointRepository walkPathPointRepository;
    private final UserRepository userRepository;
    private final WalkSpotRepository walkSpotRepository;
    private final FcmService fcmService;

    @Transactional
    public WalkStartResponse startWalk(String principalValue, WalkStartRequest request) {
        UserEntity user = getUserByPrincipal(principalValue);

        validateWalkSpot(request);

        if (walkRecordRepository.existsByUserIdAndStatus(user.getId(), WalkRecordStatus.IN_PROGRESS)) {
            throw new IllegalStateException("진행 중인 산책 기록이 이미 존재합니다.");
        }

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
    public void savePoints(String principalValue, Long walkRecordId, WalkPointRequest request) {
        UserEntity user = getUserByPrincipal(principalValue);

        WalkRecord walkRecord = walkRecordRepository.findByIdAndUserId(walkRecordId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("산책 기록을 찾을 수 없습니다."));

        if (walkRecord.getStatus() != WalkRecordStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행 중인 산책에만 좌표를 저장할 수 있습니다.");
        }

        if (request == null || request.points() == null || request.points().isEmpty()) {
            throw new IllegalArgumentException("저장할 좌표가 없습니다.");
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
    public void endWalk(String principalValue, Long walkRecordId, WalkEndRequest request) {
        UserEntity user = getUserByPrincipal(principalValue);

        WalkRecord walkRecord = walkRecordRepository.findByIdAndUserId(walkRecordId, user.getId())
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

       
        if (request.averageHealthScore() != null && request.averageHealthScore() <= 40) {

            String token = user.getFcmToken();

            if (token != null && !token.isBlank()) {
                fcmService.send(
                        token,
                        "건강 경고",
                        "현재 건강 점수가 낮습니다. 잠시 휴식을 추천드립니다."
                );
            }
        }
    }

    @Transactional(readOnly = true)
    public List<WalkRecordListResponse> getMyWalkRecords(String principalValue) {
        UserEntity user = getUserByPrincipal(principalValue);

        return walkRecordRepository.findByUserIdOrderByStartedAtDesc(user.getId()).stream()
                .map(record -> {
                    String walkSpotName = walkSpotRepository.findById(record.getWalkSpotId())
                            .map(walkSpot -> walkSpot.getName())
                            .orElse("이름 없는 산책로");

                    return new WalkRecordListResponse(
                            record.getId(),
                            record.getWalkSpotId(),
                            walkSpotName,
                            record.getStartedAt(),
                            record.getEndedAt(),
                            record.getTotalDistance(),
                            record.getDurationSeconds(),
                            record.getCalories(),
                            record.getAverageHealthScore(),
                            record.getStatus().name()
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public WalkRecordDetailResponse getWalkRecordDetail(String principalValue, Long walkRecordId) {
        UserEntity user = getUserByPrincipal(principalValue);

        WalkRecord record = walkRecordRepository.findByIdAndUserId(walkRecordId, user.getId())
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

    private void validateWalkSpot(WalkStartRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("요청 값이 없습니다.");
        }

        if (request.walkSpotId() == null) {
            throw new IllegalArgumentException("walkSpotId는 필수입니다.");
        }

        if (!walkSpotRepository.existsById(request.walkSpotId())) {
            throw new IllegalArgumentException("존재하지 않는 산책 장소입니다.");
        }
    }

    private UserEntity getUserByPrincipal(String principalValue) {
        Long userId;
        try {
            userId = Long.parseLong(principalValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("인증된 사용자 식별값이 올바르지 않습니다.");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}