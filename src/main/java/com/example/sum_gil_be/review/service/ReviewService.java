package com.example.sum_gil_be.review.service;

import com.example.sum_gil_be.review.domain.dto.ReviewRequestDto;
import com.example.sum_gil_be.review.domain.dto.ReviewResponseDto;
import com.example.sum_gil_be.review.domain.entity.ReviewEntity;
import com.example.sum_gil_be.review.repository.ReviewRepository;
import com.example.sum_gil_be.user.domain.entity.UserEntity;
import com.example.sum_gil_be.user.repository.UserRepository;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;
import com.example.sum_gil_be.walkspot.repository.WalkSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WalkSpotRepository walkSpotRepository;

    @Transactional
    public Long createReview(ReviewRequestDto requestDto, String username) {
        validateRating(requestDto.getRating());

        UserEntity user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        WalkSpot walkSpot = walkSpotRepository.findById(requestDto.getWalkSpotId())
                .orElseThrow(() -> new IllegalArgumentException("산책 장소를 찾을 수 없습니다."));

        ReviewEntity review = ReviewEntity.builder()
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .user(user)
                .walkSpot(walkSpot)
                .build();

        return reviewRepository.save(review).getId();
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDto requestDto, String username) {
        validateRating(requestDto.getRating());

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        UserEntity currentUser = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        review.updateReview(requestDto.getRating(), requestDto.getContent());
    }

    @Transactional
    public void deleteReview(Long reviewId, String username) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        UserEntity currentUser = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

    public List<ReviewResponseDto> getReviewsByWalkSpot(Long walkSpotId) {
        return reviewRepository.findAllByWalkSpotIdOrderByCreatedAtDesc(walkSpotId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<WalkSpot> getPopularPlaces() {
        Pageable topFive = PageRequest.of(0, 5);
        return reviewRepository.findPopularPlaces(topFive);
    }

    private ReviewResponseDto toResponseDto(ReviewEntity review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .walkSpotId(review.getWalkSpot().getId())
                .walkSpotName(review.getWalkSpot().getName())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .nickname(review.getUser().getNickname())
                .build();
    }

    private void validateRating(Long rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("리뷰 평점은 1점에서 5점 사이여야 합니다.");
        }
    }
}