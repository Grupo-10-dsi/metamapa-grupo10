# Stage 1: build React app
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# Ajusta REACT_APP_API_URL al gateway interno cuando construyas la imagen (puedes pasarla como build-arg)
ARG REACT_APP_API_URL=http://localhost:8089
ENV REACT_APP_API_URL=$REACT_APP_API_URL
RUN npm run build

# Stage 2: serve static files with nginx
FROM nginx:1.27-alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
