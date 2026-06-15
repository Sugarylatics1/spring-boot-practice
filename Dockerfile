# Stage 1: Build the application
FROM eclipse-temurin:26-jdk AS builder
WORKDIR /app

# Copy wrapper and configuration files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Grant execution permissions and cache dependencies
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copy source code and build the package
COPY src src
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime image
FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
