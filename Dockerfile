# 베이스 이미지 설정
FROM openjdk:21-alpine

# 빌드 시 변수 설정
ARG JAR_FILE=./build/libs/project-0.0.1-SNAPSHOT.jar

# JAR 파일을 Docker 이미지에 복사
COPY ${JAR_FILE} /app.jar

# 컨테이너 시작 명령어 설정
ENTRYPOINT ["java", "-jar", "/app.jar", "-Duser.timezone=Asia/Seoul"]
 