package com.example.sum_gil_be.user.service;

import com.example.sum_gil_be.user.domain.dto.UserRequest;
import com.example.sum_gil_be.user.domain.dto.UserResponse;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getInterestRegion()
        );
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UserRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateProfile(request.getNickname(), request.getInterestRegion());
        
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getInterestRegion()
        );
    }
}
