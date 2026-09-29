FROM node:24.21-alpine AS builder
WORKDIR /app
RUN corepack enable && corepack prepare pnpm@10.32.1 --activate
ENV CI="true"
ENV CYPRESS_INSTALL_BINARY=0

# 1. Copy ONLY the package files first to leverage Docker layer caching
COPY frontend/package.json frontend/pnpm-lock.yaml frontend/pnpm-workspace.yaml ./
# 2. Install ALL dependencies (Angular needs devDependencies to build)
RUN pnpm install --frozen-lockfile
# 3. Copy the rest of the source code
COPY frontend/ ./
ARG BUILD_CONFIGURATION=production
RUN pnpm build --output-path=./dist/ --configuration=${BUILD_CONFIGURATION}

FROM nginxinc/nginx-unprivileged:1.31-alpine AS runner
# rendered to /etc/nginx/nginx.conf on start, only PCTS_* variables are substituted (nginx's own $vars stay untouched)
ENV NGINX_ENVSUBST_OUTPUT_DIR=/etc/nginx \
    NGINX_ENVSUBST_FILTER=^PCTS_ \
    PCTS_KEYCLOAK_URL=http://localhost:8544
COPY frontend/nginx.conf /etc/nginx/templates/nginx.conf.template
COPY --from=builder /app/dist/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
