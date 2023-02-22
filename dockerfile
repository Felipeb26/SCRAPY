FROM openjdk:17-jdk-slim

WORKDIR /app

COPY src /app/src
COPY target/*.jar /app/app.jar


EXPOSE 8082
CMD ["java", "-jar", "app.jar"]
