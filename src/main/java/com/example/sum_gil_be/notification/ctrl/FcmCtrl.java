package com.example.sum_gil_be.notification.ctrl;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class FcmCtrl {

    private final UserRepository userRepository;

    @PostMapping("/me/fcm-token")
    public ResponseEntity<Void> saveToken(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body
    ) {
        Long userId = Long.parseLong(userDetails.getUsername());

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        user.updateFcmToken(body.get("token"));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}