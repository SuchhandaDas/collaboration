FROM openjdk:17-jdk-slim
COPY target/collaboration-0.0.1-SNAPSHOT.jar collaboration-app.jar
ENTRYPOINT ["java", "-jar", "collaboration-app.jar"]
