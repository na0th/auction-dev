# 1. 사용할 베이스 이미지 (OpenJDK 17)
FROM openjdk:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일을 컨테이너 내부로 복사
COPY target/*.jar app.jar

# 4. 컨테이너가 실행될 때 실행할 명령어
CMD ["java", "-jar", "app.jar"]
