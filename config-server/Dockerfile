FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]