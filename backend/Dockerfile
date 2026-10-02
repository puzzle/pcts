# syntax=docker/dockerfile:1

FROM maven:3-eclipse-temurin-25 AS base
ARG MAVEN_PROFILES=""
WORKDIR /app
COPY pom.xml .
COPY backend/pom.xml backend/
# baked into the layer, not a cache mount, the dev container recompiles offline against it
RUN mvn -B -q -f backend/pom.xml ${MAVEN_PROFILES:+-P$MAVEN_PROFILES} dependency:go-offline
COPY backend/src backend/src


FROM base AS build
RUN mvn -B -f backend/pom.xml ${MAVEN_PROFILES:+-P$MAVEN_PROFILES} clean package -Dmaven.test.skip=true -Djacoco.skip=true \
    && cp backend/target/pcts-*.jar application.jar \
    && java -Djarmode=tools -jar application.jar extract --layers --destination extracted


FROM alpine AS mvnd
ARG MVND_VERSION=1.0.6
ARG TARGETARCH
RUN apk add --no-cache curl \
    && arch=$(echo "$TARGETARCH" | sed 's/arm64/aarch64/') \
    && curl -fsSL "https://github.com/apache/maven-mvnd/releases/download/${MVND_VERSION}/maven-mvnd-${MVND_VERSION}-linux-${arch}.tar.gz" \
      | tar -xz -C /opt \
    && mv "/opt/maven-mvnd-${MVND_VERSION}-linux-${arch}" /opt/mvnd


FROM base AS dev
COPY --from=mvnd /opt/mvnd /opt/mvnd


FROM eclipse-temurin:25.0.4_7-jre-alpine AS runner
RUN apk add --no-cache curl
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/extracted/dependencies/ ./
COPY --from=build /app/extracted/spring-boot-loader/ ./
COPY --from=build /app/extracted/snapshot-dependencies/ ./
COPY --from=build /app/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]
