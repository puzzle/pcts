# syntax=docker/dockerfile:1

# Stages:
#   build  - compiles and packages everything, MAVEN_PROFILES selects the Maven profile(s):
#            ''  -> release build (default, CI)
#            dev -> devtools + changed-files-only compile, required by the dev stage (set by docker compose)
#   mvnd   - downloads mvnd for the dev stage, independent of the build so it stays cached
#   dev    - used by docker compose: runs from target/classes with devtools + JDWP (5005), sources are synced
#            in by 'docker compose up --watch' and recompiled by a warm mvnd daemon (see the develop.watch rules)
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
ARG MAVEN_PROFILES
RUN case ",$MAVEN_PROFILES," in *,dev,*) ;; \
      *) echo "the dev stage needs --build-arg MAVEN_PROFILES=dev" >&2; exit 1 ;; esac
# mvnd is copied from its own stage, so a code change doesn't download it again
COPY --from=mvnd /opt/mvnd /opt/mvnd
RUN ln -s /opt/mvnd/bin/mvnd /usr/local/bin/mvnd
COPY --chmod=755 backend/docker/dev-recompile.sh /usr/local/bin/dev-recompile

# devtools only restarts once a recompile is complete (dev-recompile touches the trigger file), so the
# poll/quiet period can be short without restarting on a half written target/classes
ENV SPRING_DEVTOOLS_RESTART_TRIGGER_FILE=.reloadtrigger \
    SPRING_DEVTOOLS_RESTART_POLL_INTERVAL=200ms \
    SPRING_DEVTOOLS_RESTART_QUIET_PERIOD=50ms \
    SPRING_DEVTOOLS_LIVERELOAD_ENABLED=false
EXPOSE 8080 5005
# every (re)start does a full compile, so a pom.xml change (compose restarts the container) or a
# 'docker compose restart pcts-backend' leaves no stale classes. The compile also warms up the mvnd daemon used by the
# watch recompiles, the app itself is started by plain mvn so it doesn't occupy that daemon. Not offline, a pom.xml
# change can add dependencies. SPRING_PROFILES_ACTIVE is passed on (dev, or e2e-test for local e2e),
# maven.test.skip stops spring-boot:run from compiling the tests on every start
CMD ["sh", "-c", "dev-recompile --full && exec mvn -B -f backend/pom.xml -Pdev -Dmaven.test.skip=true -Ddev.debug.address='*:5005' -Dspring-boot.run.profiles=\"${SPRING_PROFILES_ACTIVE:-dev}\" spring-boot:run"]


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
