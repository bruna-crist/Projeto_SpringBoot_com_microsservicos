FROM maven:3.8.6-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/ms-event-manager-0.0.1-SNAPSHOT.jar ms-event-manager.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ms-event-manager.jar"]