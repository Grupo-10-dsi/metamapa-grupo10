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
# Tomamos el JAR ya construido desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Puerto expuesto por el servicio
EXPOSE 8088
# Arranque del servicio
ENTRYPOINT ["java","-jar","/app/app.jar"]
