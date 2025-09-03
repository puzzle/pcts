FROM node:24.0-alpine AS build

WORKDIR /app

COPY package*.json /app/

RUN npm install

COPY . /app

RUN npm run build



FROM node:24.0-alpine
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY --from=build /app/dist /usr/src/app
ENV PORT 4200
CMD [ "node", "index.js" ]
