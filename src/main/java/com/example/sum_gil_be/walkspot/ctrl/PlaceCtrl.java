package com.example.sum_gil_be.walkspot.ctrl;

import com.example.sum_gil_be.walkspot.domain.dto.InfrastructureResponse;
import com.example.sum_gil_be.walkspot.domain.dto.PlaceDetailResponse;
import com.example.sum_gil_be.walkspot.domain.dto.PlaceListResponse;
import com.example.sum_gil_be.walkspot.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@Tag(name = "Place API", description = "산책 장소 조회 및 주변 편의시설 조회 API")
public class PlaceCtrl {

    private final PlaceService placeService;

    @Operation(
            summary = "주변 산책 장소 목록 조회",
            description = "사용자의 현재 위도(latitude), 경도(longitude)를 기준으로 반경 내 산책 장소 목록을 거리순으로 조회합니다. "
                    + "반경 내 데이터가 없으면 가장 가까운 산책 장소 3개를 반환합니다."
    )
    @GetMapping
    public ResponseEntity<List<PlaceListResponse>> getNearbyPlaces(
            @Parameter(description = "현재 위치의 위도", example = "37.5665")
            @RequestParam double latitude,

            @Parameter(description = "현재 위치의 경도", example = "126.9780")
            @RequestParam double longitude,

            @Parameter(description = "검색 반경(미터), 기본값 3000", example = "3000")
            @RequestParam(defaultValue = "3000") double radius
    ) {
        return ResponseEntity.ok(placeService.getNearbyPlaces(latitude, longitude, radius));
    }

    @Operation(
            summary = "산책 장소 상세 조회",
            description = "placeId에 해당하는 산책 장소의 상세 정보(이름, 주소, 설명, 좌표, 안전 점수 등)를 조회합니다."
    )
    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceDetailResponse> getPlaceDetail(
            @Parameter(description = "산책 장소 ID", example = "1")
            @PathVariable Long placeId
    ) {
        return ResponseEntity.ok(placeService.getPlaceDetail(placeId));
    }

    @Operation(
            summary = "산책 장소 주변 편의시설 조회",
            description = "산책 장소 기준 반경 내 편의시설을 조회합니다. "
                    + "type을 지정하지 않으면 카페, 편의점, 화장실을 통합 조회합니다. "
                    + "type 값은 CAFE, CONVENIENCE_STORE, TOILET 중 하나를 사용할 수 있습니다."
    )
    @GetMapping("/{placeId}/infrastructures")
    public ResponseEntity<List<InfrastructureResponse>> getInfrastructures(
            @Parameter(description = "산책 장소 ID", example = "1")
            @PathVariable Long placeId,
            @Parameter(
                    description = "편의시설 타입. 미입력 시 전체 조회",
                    example = "TOILET, CAFE ..."
            )
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(placeService.getInfrastructures(placeId, type));
    }
}