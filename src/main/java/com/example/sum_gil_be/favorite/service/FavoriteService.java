package com.example.sum_gil_be.favorite.service;

import com.example.sum_gil_be.favorite.domain.dto.FavoriteRequest;
import com.example.sum_gil_be.favorite.domain.dto.FavoriteResponse;
import com.example.sum_gil_be.favorite.domain.entity.FavoriteEntity;
import com.example.sum_gil_be.favorite.repository.FavoriteRepository;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final WalkSpotRepository walkSpotRepository;

    @Transactional
    public Long addFavorite(Long userId, FavoriteRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        WalkSpot walkSpot = walkSpotRepository.findById(request.getWalkSpotId())
                .orElseThrow(() -> new RuntimeException("산책 장소를 찾을 수 없습니다."));

        // 중복 체크
        if (favoriteRepository.existsByUserAndWalkSpot(user, walkSpot)) {
            throw new RuntimeException("이미 즐겨찾기에 등록된 장소입니다.");
        }

        FavoriteEntity favorite = FavoriteEntity.builder()
                .user(user)
                .walkSpot(walkSpot)
                .build();

        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> getMyFavorites(Long userId) {
        return favoriteRepository.findAllByUserId(userId).stream()
                .map(f -> FavoriteResponse.builder()
                        .id(f.getId())
                        .walkSpotId(f.getWalkSpot().getId())
                        .name(f.getWalkSpot().getName())
                        .address(f.getWalkSpot().getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long userId, Long favoriteId) {
        FavoriteEntity favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("즐겨찾기 항목을 찾을 수 없습니다."));

        // 본인 확인 (보안)
        if (!favorite.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        favoriteRepository.delete(favorite);
    }
}