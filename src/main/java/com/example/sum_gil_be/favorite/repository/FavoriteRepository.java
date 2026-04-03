package com.example.sum_gil_be.favorite.repository;


import com.example.sum_gil_be.favorite.domain.entity.FavoriteEntity;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findAllByUserId(Long userId);

    boolean existsByUserAndWalkSpot(UserEntity user, WalkSpot walkSpot);
    
    long countByUserId(Long userId);
}