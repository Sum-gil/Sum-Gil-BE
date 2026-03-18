package com.example.sum_gil_be.favorite.ctrl;

import com.example.sum_gil_be.favorite.domain.dto.FavoriteRequest;
import com.example.sum_gil_be.favorite.domain.dto.FavoriteResponse;
import com.example.sum_gil_be.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite", description = "즐겨찾기 관련 API")
public class FavoriteCtrl {

    private final FavoriteService favoriteService;

    @PostMapping
    @Operation(summary = "즐겨찾기 등록", description = "특정 산책 장소를 즐겨찾기에 추가합니다.")
    public ResponseEntity<Long> addFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FavoriteRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(favoriteService.addFavorite(userId, request));
    }

    @GetMapping
    @Operation(summary = "즐겨찾기 목록 조회", description = "현재 로그인한 사용자의 즐겨찾기 리스트를 조회합니다.")
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(favoriteService.getMyFavorites(userId));
    }

    @DeleteMapping("/{favoriteId}")
    @Operation(summary = "즐겨찾기 삭제", description = "등록된 즐겨찾기 항목을 삭제합니다.")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long favoriteId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        favoriteService.deleteFavorite(userId, favoriteId);
        return ResponseEntity.noContent().build();
    }
}