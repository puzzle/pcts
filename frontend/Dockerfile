FROM node:24.13-alpine AS builder
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend ./
RUN npm run build -- --output-path=./dist/ --configuration=production
# Print add_header Content-Security-Policy
# copy all values from csp.txt into file
# add generated hashes from angular
RUN (printf 'add_header Content-Security-Policy "' && \
     tr -d '\n' < csp.txt && \
     printf " " && \
     grep "Content-Security-Policy" "dist/browser/index.html" | sed -nE 's/.*content="([^"]*)".*/\1/p' | tr -d '\n' && \
     printf '";\n') > final-csp.txt

FROM nginxinc/nginx-unprivileged:1.29-alpine AS runner
COPY frontend/nginx.conf /etc/nginx/
COPY --from=builder /app/dist/browser /usr/share/nginx/html
# Copy the csp header file to the same location as the nginx conf
COPY --from=builder /app/final-csp.txt /etc/nginx/
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
