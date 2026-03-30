package com.example.sum_gil_be.notification.service;

import com.example.sum_gil_be.record.domain.entity.WalkRecord;
import com.example.sum_gil_be.record.domain.entity.WalkRecordStatus;
import com.example.sum_gil_be.record.repository.WalkRecordRepository;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalkNotificationScheduler {

    private final WalkRecordRepository walkRecordRepository;
    private final FcmService fcmService;

    @Transactional
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkWalkNotifications() {
        log.info("스케줄러 실행");

        List<WalkRecord> records = walkRecordRepository.findByStatus(WalkRecordStatus.IN_PROGRESS);
        log.info("진행 중 산책 수 = {}", records.size());

        for (WalkRecord record : records) {
            long minutes = Duration.between(record.getStartedAt(), LocalDateTime.now()).toMinutes();

            UserEntity user = record.getUser();
            String token = user.getFcmToken();

            log.info("recordId={}, minutes={}, tokenExists={}, notified10m={}, notified20m={}, notified30m={}",
                    record.getId(),
                    minutes,
                    token != null && !token.isBlank(),
                    record.getNotified10m(),
                    record.getNotified20m(),
                    record.getNotified30m());

            if (token == null || token.isBlank()) {
                continue;
            }

            boolean updated = false;

            if (minutes >= 10 && !Boolean.TRUE.equals(record.getNotified10m())) {
                log.info("10분 알림 전송 시도: recordId={}", record.getId());

                boolean sent = fcmService.send(token, "산책 알림", "산책한지 10분 지났습니다.");
                if (sent) {
                    record.mark10mNotified();
                    updated = true;
                    log.info("10분 알림 전송 성공 및 저장 대상 반영: recordId={}", record.getId());
                } else {
                    log.warn("10분 알림 전송 실패: recordId={}", record.getId());
                }
            }

            if (minutes >= 20 && !Boolean.TRUE.equals(record.getNotified20m())) {
                log.info("20분 알림 전송 시도: recordId={}", record.getId());

                boolean sent = fcmService.send(token, "산책 알림", "산책한지 20분 지났습니다.");
                if (sent) {
                    record.mark20mNotified();
                    updated = true;
                    log.info("20분 알림 전송 성공 및 저장 대상 반영: recordId={}", record.getId());
                } else {
                    log.warn("20분 알림 전송 실패: recordId={}", record.getId());
                }
            }

            if (minutes >= 30 && !Boolean.TRUE.equals(record.getNotified30m())) {
                log.info("30분 알림 전송 시도: recordId={}", record.getId());

                boolean sent = fcmService.send(token, "산책 알림", "산책한지 30분 지났습니다.");
                if (sent) {
                    record.mark30mNotified();
                    updated = true;
                    log.info("30분 알림 전송 성공 및 저장 대상 반영: recordId={}", record.getId());
                } else {
                    log.warn("30분 알림 전송 실패: recordId={}", record.getId());
                }
            }

            if (updated) {
                walkRecordRepository.save(record);
                log.info("알림 상태 저장 완료: recordId={}", record.getId());
            }
        }
    }
}