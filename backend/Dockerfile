FROM maven:3-eclipse-temurin-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:25-jre-alpine AS runner
RUN apk update && apk add --upgrade curl
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]