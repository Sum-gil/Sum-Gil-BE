# 🚶‍♂️ sum-gil-BE

> **AI 기반 산책 추천 및 건강·안전 분석 백엔드 서비스**  
> 사용자의 위치를 기반으로 환경 데이터를 분석하여  
> 최적의 산책 경로와 건강·안전 정보를 제공합니다.

---

## 📦 Repositories

[![Frontend](https://img.shields.io/badge/Frontend-sumgil--FE-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://github.com/Sum-gil/Sum-Gil-FE)
[![Backend](https://img.shields.io/badge/Backend-sumgil--BE-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://github.com/Sum-gil/Sum-Gil-BE)

---

<details>
<summary><h2>📂 Project Structure</h2></summary>

```bash
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂sum_gil_be
 ┃ ┃ ┃ ┃ ┃ ┣ 📂ai
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OpenAiConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiRecommendationCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiRecommendationRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiRecommendationResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ParsedRecommendationCondition.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RecommendedPlaceDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AiRecommendationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OpenAiQueryParserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoUserInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SocialLoginRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CorsConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FcmConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JacksonConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SwaggerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dashboard
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂client
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AirKoreaClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoLocalClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SeoulPopulationClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WeatherClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoLocalConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoLocalProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DashboardCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kakao
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoCoord2RegionResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AirQualityInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EnvironmentResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PopulationInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RecommendationResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RecommendedPlaceResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ResolvedRegion.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SeoulPopulationApiResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SeoulPopulationRow.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WeatherInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DashboardService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EnvironmentService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RecommendationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RegionResolveService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SeoulPopulationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GridPoint.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WeatherBaseTimeUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WeatherGridConverter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂favorite
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mypage
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MyPageCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MyPageSummaryResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MyPageService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂notification
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FcmCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FcmService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkNotificationScheduler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂record
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkRecordCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkEndRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkPathPointResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkPointItem.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkPointRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkRecordDetailResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkRecordListResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkStartRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkStartResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkPathPoint.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkRecord.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkRecordStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜WalkPathPointRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkRecordRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkRecordService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂report
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReportCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MonthlyReportResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkReportResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReportService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂walkspot
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoLocalProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PlaceCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CctvPoint.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HealthScoreResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InfrastructureResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoPlaceSearchResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceDetailResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceListResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SafetyScoreResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkSpot.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkSpotRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CctvCsvService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CctvScoreService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoLocalSearchClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceScoreService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PlaceService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WalkSpotCsvService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GeoUtils.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SumGilApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂data
 ┃ ┃ ┃ ┣ 📜seoul_cctv.csv
 ┃ ┃ ┃ ┗ 📜walkspot.csv
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┣ 📜application.yaml
 ┃ ┃ ┗ 📜sum-gil-firebase-adminsdk-fbsvc-2282a0ccae.json
 ┗ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂sum_gil_be
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SumGilApplicationTests.java
```
</details>

## 🔗 Tech Stack

### Language & Framework
![Java](https://img.shields.io/badge/Java%2017-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Security-6DB33F?style=for-the-badge)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge)

### Database
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb)
![AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge)

### External API
![Kakao](https://img.shields.io/badge/Kakao%20API-FFCD00?style=for-the-badge)
![PublicData](https://img.shields.io/badge/Public%20API-009688?style=for-the-badge)

### Notification
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase)

### Infrastructure
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge)

### Docs
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge)

### Security
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge)
![OAuth2](https://img.shields.io/badge/OAuth2-blue?style=for-the-badge)

---

## 📌 Key APIs

| Feature | Method | Endpoint | Description |
|---|---|---|---|
| Health | GET | `/health` | 서버 상태 확인 |
| Auth | GET | `/api/auth/oauth/kakao` | 카카오 로그인 |
| Place | GET | `/api/places` | 주변 산책로 조회 |
| Place | GET | `/api/places/{id}` | 산책로 상세 |
| Place | GET | `/api/places/{id}/health-score` | 건강 점수 |
| Place | GET | `/api/places/{id}/safety` | 안전 점수 |
| Dashboard | GET | `/api/dashboard/environment` | 환경 분석 |
| Dashboard | GET | `/api/dashboard/recommendations` | 추천 산책로 |
| Walk | POST | `/api/walk-records/start` | 산책 시작 |
| Walk | POST | `/api/walk-records/{id}/end` | 산책 종료 |
| Walk | GET | `/api/walk-records` | 산책 기록 |
| Report | GET | `/api/reports/walks/{id}` | 산책 리포트 |
| Favorite | POST | `/api/favorites` | 즐겨찾기 |
| Review | POST | `/api/reviews` | 리뷰 작성 |

---

## 🚀 Deployment



## ☁️ Infrastructure

> 본 서비스는 AWS EC2 기반으로 운영되며  
> Nginx Reverse Proxy를 통해 HTTPS 환경을 구성했습니다.  
>  
> 외부 요청은 Nginx를 통해 Backend로 전달되며  
> 데이터는 RDS(MariaDB)에 저장됩니다.  
>  
> 외부 API(기상청, 에어코리아, 카카오, Population API)를 활용하여  
> 실시간 환경 분석 및 추천 기능을 제공합니다.

---

## 🔄 CI / CD

### CI
- GitHub Actions 기반 자동 빌드
- Docker 이미지 생성

### CD
- EC2 서버 자동 배포
- Docker 컨테이너 실행

---

## 🔐 Authentication & Authorization

### JWT Authentication
- Access / Refresh Token 구조
- Stateless 인증

### Spring Security
- JWT 필터 기반 인증 처리

### OAuth2 (Kakao)
- 카카오 로그인 → JWT 발급
- 기존 인증과 통합

---

## 🔔 Notification

- Firebase Cloud Messaging 사용
- 산책 중 알림 제공

---

## ⚙️ Core Logic

### Health Score
```
healthScore =
airQuality * 0.5 +
greenRatio * 0.25 +
crowd * 0.25
```

### Safety Score
```
safetyScore =
population * 0.7 +
nightSafe * 0.3
```

---

## 🤖 AI Recommendation

사용자의 자연어 입력을 기반으로 조건을 해석하고  
적합한 산책로를 추천하는 기능을 제공합니다.

### 주요 흐름

1. 사용자 입력 (예: "조용하고 공기 좋은 산책로 추천해줘")
2. OpenAI를 활용하여 조건 파싱
3. ParsedRecommendationCondition 생성
4. 조건 기반 DB 조회 및 점수 계산
5. 최적의 산책로 추천 결과 반환

### 구성 요소

- `AiRecommendationCtrl` : API 엔드포인트
- `AiRecommendationService` : 추천 로직 처리
- `OpenAiQueryParserService` : 자연어 → 조건 변환
- `RecommendedPlaceDto` : 추천 결과 반환

### 특징

- 자연어 기반 추천 시스템
- 환경 데이터 + DB 데이터 결합
- 사용자 맞춤형 산책 경로 제공

---


## 📊 Features Summary

-  위치 기반 산책로 추천
-  건강 점수 분석
-  안전 점수 제공
-  산책 기록 및 리포트
-  즐겨찾기 및 리뷰
-  실시간 알림

---

<div align=center>
	<h1>👨‍💻 BE Developers</h1>
	
| <img src="https://github.com/rudals2334.png" width="80"> | <img src="https://github.com/M4rs0312.png" width="80"> |
| :--------------------------------------------------: | :--------------------------------------------------: |
| [이경민](https://github.com/rudals2334) | [정회성](https://github.com/M4rs0312) |
|  Notification (FCM) · CI/CD <br> AI Recommendation · tracker | Auth · JWT · Kakao Login <br> Deployment · Production Config|

</div>
