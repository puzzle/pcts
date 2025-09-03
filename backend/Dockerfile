FROM maven:3-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn clean install

COPY src ./src

RUN mvn package

FROM openjdk

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/*.jar pcts
