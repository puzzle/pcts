FROM node:24.16-alpine AS builder
WORKDIR /app
RUN corepack enable
COPY frontend/package.json frontend/pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY frontend ./
ARG BUILD_CONFIGURATION=production
RUN npm run build -- --output-path=./dist/ --configuration=${BUILD_CONFIGURATION}

FROM nginxinc/nginx-unprivileged:1.31-alpine AS runner
COPY frontend/nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
