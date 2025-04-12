# Use Maven image to build the app
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/collaboration-0.0.1-SNAPSHOT.jar collaboration-app.jar
ENTRYPOINT ["java", "-jar", "collaboration-app.jar"]

