package com.example.sum_gil_be.review.ctrl;

import com.example.sum_gil_be.review.domain.dto.ReviewRequestDto;
import com.example.sum_gil_be.review.domain.dto.ReviewResponseDto;
import com.example.sum_gil_be.review.service.ReviewService;
import com.example.sum_gil_be.walkspot.domain.entity.WalkSpot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review", description = "리뷰 관리 및 커뮤니티 API")
@RestController
@RequiredArgsConstructor
public class ReviewCtrl {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "산책 장소에 대한 리뷰를 작성합니다.")
    @PostMapping("/api/reviews")
    public ResponseEntity<Long> createReview(
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long reviewId = reviewService.createReview(requestDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

    @Operation(summary = "리뷰 목록 조회", description = "특정 산책 장소의 모든 리뷰를 최신순으로 조회합니다.")
    @GetMapping("/api/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@RequestParam("walkSpotId") Long walkSpotId) {
        return ResponseEntity.ok(reviewService.getReviewsByWalkSpot(walkSpotId));
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰의 평점과 내용을 수정합니다.")
    @PatchMapping("/api/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        reviewService.updateReview(reviewId, requestDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        reviewService.deleteReview(reviewId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "인기 산책 장소 조회", description = "리뷰가 가장 많이 달린 순으로 인기 산책 장소를 조회합니다.")
    @GetMapping("/api/community/popular-places")
    public ResponseEntity<List<WalkSpot>> getPopularPlaces() {
        return ResponseEntity.ok(reviewService.getPopularPlaces());
    }
}