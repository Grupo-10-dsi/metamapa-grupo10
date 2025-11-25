## Etapa de build: usa Maven + JDK para compilar el JAR
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copiar el pom es útil para aprovechar el cacheo de capas de Docker
COPY pom.xml . 
# Copiamos el código fuente
COPY src ./src
# Compilamos y generamos el JAR (sin tests para acelerar)
RUN mvn -B -DskipTests clean package

## Etapa runtime: imagen liviana con solo JRE
FROM eclipse-temurin:17-jre
WORKDIR /app

# --- INICIO DE MODIFICACIONES ---
# Instalar curl (esta imagen usa 'apt-get') y limpiar caché
USER root
RUN apt-get update && apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Copiar el script de espera (debe estar en esta misma carpeta)
COPY wait-for-keycloak.sh /app/wait-for-keycloak.sh
RUN chmod +x /app/wait-for-keycloak.sh
# --- FIN DE MODIFICACIONES ---

# Tomamos el JAR ya construido desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto por el servicio
EXPOSE 8089

# CAMBIO: El Entrypoint ahora es el script de espera.
# Este script se encargará de ejecutar "java -jar /app/app.jar" cuando Keycloak esté listo.
ENTRYPOINT ["/app/wait-for-keycloak.sh"]

