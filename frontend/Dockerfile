FROM node:24.0-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build -- --output-path=./dist/ --configuration=production

FROM nginx:1.29.1-alpine AS runner
COPY nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
