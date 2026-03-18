package com.example.sum_gil_be.favorite.repository;


import com.example.sum_gil_be.favorite.domain.entity.FavoriteEntity;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    // 특정 사용자의 즐겨찾기 목록 전체 조회
    List<FavoriteEntity> findAllByUserId(Long userId);

    // 중복 등록 방지를 위한 체크
    boolean existsByUserAndWalkSpot(UserEntity user, WalkSpot walkSpot);
}