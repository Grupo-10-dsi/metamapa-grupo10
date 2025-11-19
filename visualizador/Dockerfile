# Stage 1: build React app
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# Variables de entorno para el build de React
ARG REACT_APP_API_GATEWAY_URL_BASE
ARG REACT_APP_KEYCLOAK_URL_BASE
ARG REACT_APP_KEYCLOAK_CLIENT_ID
ARG REACT_APP_API_URL

ENV REACT_APP_API_GATEWAY_URL_BASE=$REACT_APP_API_GATEWAY_URL_BASE
ENV REACT_APP_KEYCLOAK_URL_BASE=$REACT_APP_KEYCLOAK_URL_BASE
ENV REACT_APP_KEYCLOAK_CLIENT_ID=$REACT_APP_KEYCLOAK_CLIENT_ID
ENV REACT_APP_API_URL=$REACT_APP_API_URL

RUN npm run build

# Stage 2: serve static files with nginx
FROM nginx:1.27-alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
