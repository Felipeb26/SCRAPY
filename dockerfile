FROM adoptopenjdk/openjdk14:x86_64-alpine-jre-14.0.2_12

WORKDIR /app

COPY target/*.jar /app/app.jar
COPY drivers /app/drivers

EXPOSE 8082
CMD ["java", "-jar", "app.jar"]
