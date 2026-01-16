FROM maven:3-eclipse-temurin-25-alpine AS builder
WORKDIR /app

COPY pom.xml .

COPY backend/pom.xml backend/
COPY backend/src backend/src

RUN mvn -f backend/pom.xml clean install -DskipTests

FROM eclipse-temurin:25-jre-alpine AS runner
RUN apk update && apk add --upgrade curl
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/backend/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]