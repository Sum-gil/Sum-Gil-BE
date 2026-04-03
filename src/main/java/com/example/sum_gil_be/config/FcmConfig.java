package com.example.sum_gil_be.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FcmConfig {

    private static final String FIREBASE_CONFIG_PATH = "/app/firebase.json";

    @PostConstruct
    public void initialize() {
        try (FileInputStream serviceAccount = new FileInputStream(FIREBASE_CONFIG_PATH)) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            log.info("Fcm 설정 성공");
        } catch (IOException exception) {
            log.error("Fcm 연결 오류 {}", exception.getMessage(), exception);
        }
    }
}