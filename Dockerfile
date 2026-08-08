FROM eclipse-temurin:25-jdk-alpine AS builder
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean :main-server:bootJar

FROM eclipse-temurin:25-jre-alpine
COPY . .
COPY --from=builder /main-server/build/libs/main-server.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]