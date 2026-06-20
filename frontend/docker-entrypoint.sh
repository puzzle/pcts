#!/bin/sh
set -e

cat > /usr/share/nginx/html/config.json <<EOF
{
  "apiUrl": "${API_URL:-http://pcts-backend:8080}",
  "keycloak": {
    "url": "${KEYCLOAK_URL:-https://sso.puzzle.ch/auth}",
    "realm": "${KEYCLOAK_REALM:-pitc}",
    "clientId": "${KEYCLOAK_CLIENT_ID:-pitc_pcts_staging}",
    "adminAuthorities": ${KEYCLOAK_ADMIN_AUTHORITIES:-[]}
  }
}
EOF

# Extract the needed origin by removing anything after https://sso.puzzle.ch (or similar)
KEYCLOAK_ORIGIN=$(echo "${KEYCLOAK_URL:-https://sso.puzzle.ch/auth}" | sed 's|\(https*://[^/]*\).*|\1|')
cat > /etc/nginx/conf.d/csp.conf <<EOF
set \$csp_header "script-src 'nonce-\$request_id' 'self'; style-src 'self' 'sha256-mwH/Oz1bMiZ9vHH84YJ6PbP6BpLW5nG9AD9Lad8u1+c=' 'nonce-\$request_id'; default-src 'self'; object-src 'none'; base-uri 'self'; connect-src 'self' ${KEYCLOAK_ORIGIN}; font-src 'self'; frame-src 'self' ${KEYCLOAK_ORIGIN}; img-src 'self'; manifest-src 'self'; media-src 'self'; worker-src 'none';";
EOF

exec nginx -g 'daemon off;'
