FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Firebase 서비스 계정 키 복사
COPY sum-gil-firebase-adminsdk-fbsvc-2282a0ccae.json /app/sum-gil-firebase-adminsdk-fbsvc-2282a0ccae.json

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app/app.jar"]