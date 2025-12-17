# =========================
# 1단계: build
# =========================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# =========================
# 2단계: runtime
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

RUN apt-get update && apt-get install -y \
    curl \
    tesseract-ocr \
    tesseract-ocr-eng \
    tesseract-ocr-kor \
    tesseract-ocr-jpn \
    tesseract-ocr-chi-sim \
    fontconfig \
 && rm -rf /var/lib/apt/lists/*

# 빌드된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]