FROM node:24.11-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build -- --output-path=./dist/ --configuration=production

# give permissions for nginx
USER root
RUN mkdir -p /var/cache/nginx /var/run/nginx \
    && chmod -R 777 /var/cache/nginx /var/run/nginx
USER nginx

FROM nginx:1.29.3-alpine AS runner
COPY nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
