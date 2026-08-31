FROM maven:3-eclipse-temurin-25-alpine AS builder
WORKDIR /app

COPY pom.xml .

COPY migration/pom.xml migration/
COPY migration/src migration/src

RUN mvn -f migration/pom.xml clean install -DskipTests

FROM eclipse-temurin:25.0.4_7-jre-alpine AS runner
RUN apk update && apk add --upgrade curl
WORKDIR /app
EXPOSE 8081
COPY --from=builder /app/migration/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]