docker compose stop pcts-keycloak
docker commit pcts-keycloak kc-export-tmp

docker run --rm -v "$PWD/config:/out" \
  --entrypoint /opt/keycloak/bin/kc.sh \
  kc-export-tmp \
  export --file /out/realm-export-pitc.json --realm pitc --users same_file

docker rmi kc-export-tmp
docker compose start pcts-keycloak
