package com.example.sum_gil_be.notification.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FcmService {

    public boolean send(String token, String title, String body) {
        try {
            log.info("FCM 전송 시작 token={}, title={}", token, title);

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(
                            Notification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build()
                    )
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 전송 성공 response={}", response);
            return true;

        } catch (Exception e) {
            log.error("FCM 전송 실패", e);
            return false;
        }
    }
}