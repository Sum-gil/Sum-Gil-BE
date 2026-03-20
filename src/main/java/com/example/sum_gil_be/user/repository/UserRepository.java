package com.example.sum_gil_be.user.repository;

import com.example.sum_gil_be.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByKakaoId(String kakaoId);

}
