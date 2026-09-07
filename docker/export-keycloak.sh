#!/usr/bin/env bash
# Stop keycloak, export current configuration and restart keycloak again

docker compose stop pcts-keycloak
docker commit pcts-keycloak kc-export-tmp

docker run --rm -v "$PWD/config:/out" \
  --user root \
  --entrypoint /bin/bash \
  kc-export-tmp \
  -c "/opt/keycloak/bin/kc.sh export --file /out/realm-export-pitc.json --realm pitc --users same_file && chown $(id -u):$(id -g) /out/realm-export-pitc.json"

docker rmi kc-export-tmp
docker compose start pcts-keycloak