FROM node:24.18-alpine AS builder
WORKDIR /app
RUN corepack enable && corepack prepare pnpm@10.32.1 --activate
COPY frontend/package.json frontend/pnpm-lock.yaml frontend/pnpm-workspace.yaml ./
RUN pnpm install --frozen-lockfile
COPY frontend ./
ARG BUILD_CONFIGURATION=production
RUN pnpm run build --output-path=./dist/ --configuration=${BUILD_CONFIGURATION}

FROM nginxinc/nginx-unprivileged:1.31-alpine AS runner
COPY frontend/nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html

# User root user to allow nginx user to execute entrypoint script
USER root
COPY frontend/docker-entrypoint.sh /docker-entrypoint.sh
RUN touch /usr/share/nginx/html/config.json && \
    chown nginx:nginx /usr/share/nginx/html/config.json && \
    touch /etc/nginx/conf.d/csp.conf && \
    chown nginx:nginx /etc/nginx/conf.d/csp.conf && \
    chmod +x /docker-entrypoint.sh

# Switch back to non-root user
USER nginx
EXPOSE 4200
ENTRYPOINT ["/docker-entrypoint.sh"]
