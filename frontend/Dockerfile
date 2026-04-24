FROM node:24.15-alpine AS builder
WORKDIR /app
RUN corepack enable
COPY frontend/package.json frontend/pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY frontend ./
RUN pnpm run build -- --output-path=./dist/ --configuration=production

FROM nginxinc/nginx-unprivileged:1.30-alpine AS runner
COPY frontend/nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
