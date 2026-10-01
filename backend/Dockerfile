# syntax=docker/dockerfile:1

# Stages:
#   build  - compiles and packages everything, MAVEN_PROFILES selects the Maven profile(s):
#            ''  -> release build (default, CI)
#            dev -> devtools + changed-files-only compile, for the dev stage (set by docker compose)
#   mvnd   - downloads mvnd for the dev stage, independent of the build so it stays cached
#   dev    - build stage + mvnd, used by docker compose which runs it from target/classes with devtools
#   runner - default (last) stage, release image

FROM maven:3-eclipse-temurin-25 AS build
ARG MAVEN_PROFILES=""
WORKDIR /app
COPY pom.xml .
COPY backend/pom.xml backend/
# dependencies are baked into the layer instead of a cache mount so the dev container can recompile offline,
# the layer is only invalidated by a pom.xml change
RUN mvn -B -q -f backend/pom.xml ${MAVEN_PROFILES:+-P$MAVEN_PROFILES} dependency:go-offline
COPY backend/src backend/src
RUN mvn -B -f backend/pom.xml ${MAVEN_PROFILES:+-P$MAVEN_PROFILES} clean package -Dmaven.test.skip=true -Djacoco.skip=true \
    && cp backend/target/pcts-*.jar application.jar \
    && java -Djarmode=tools -jar application.jar extract --layers --destination extracted


FROM alpine AS mvnd
ARG MVND_VERSION=1.0.6
ARG TARGETARCH
# mvnd ships a native client per architecture, named amd64 / aarch64 (e.g. Apple Silicon)
RUN apk add --no-cache curl \
    && arch=$(echo "$TARGETARCH" | sed 's/arm64/aarch64/') \
    && curl -fsSL "https://github.com/apache/maven-mvnd/releases/download/${MVND_VERSION}/maven-mvnd-${MVND_VERSION}-linux-${arch}.tar.gz" \
      | tar -xz -C /opt \
    && mv "/opt/maven-mvnd-${MVND_VERSION}-linux-${arch}" /opt/mvnd


FROM build AS dev
# mvnd is copied from its own stage, so a code change doesn't download it again. Everything else of the dev setup
# (start command, devtools settings, dev-recompile script) is in docker/docker-compose.yml
COPY --from=mvnd /opt/mvnd /opt/mvnd


FROM eclipse-temurin:25.0.4_7-jre-alpine AS runner
RUN apk add --no-cache curl
WORKDIR /app
EXPOSE 8080
# least to most frequently changing, so a code change only replaces the application layer
COPY --from=build /app/extracted/dependencies/ ./
COPY --from=build /app/extracted/spring-boot-loader/ ./
COPY --from=build /app/extracted/snapshot-dependencies/ ./
COPY --from=build /app/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]
