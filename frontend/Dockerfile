FROM node:24.18-alpine AS builder
WORKDIR /app
RUN corepack enable && corepack prepare pnpm@10.32.1 --activate
COPY frontend/package.json frontend/pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY frontend ./
ARG BUILD_CONFIGURATION=production
RUN pnpm run build --output-path=./dist/ --configuration=${BUILD_CONFIGURATION}

FROM nginxinc/nginx-unprivileged:1.31-alpine AS runner
COPY frontend/nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
