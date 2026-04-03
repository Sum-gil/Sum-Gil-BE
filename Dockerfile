# 1. 베이스 이미지 설정 (Java 17 환경)
FROM amazoncorretto:17-alpine-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 jar 파일을 컨테이너 내부로 복사
# (build/libs 폴더 안에 생성된 .jar 파일을 app.jar라는 이름으로 복사함)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. 컨테이너 실행 시 자바 애플리케이션 실행
# (-Duser.timezone=Asia/Seoul 설정을 추가해 서버 시간을 한국으로 맞춤)
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app/app.jar"]