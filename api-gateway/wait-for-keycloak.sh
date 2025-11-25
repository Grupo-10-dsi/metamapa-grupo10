#!/bin/sh

# URL del realm que estamos esperando.
# Usa variable de entorno o fallback a Docker Compose local
KEYCLOAK_URL="${KEYCLOAK_URL:-http://keycloak:9090/realms/MetaMapa}"

echo "------ Esperando a Keycloak en $KEYCLOAK_URL ------"

# Bucle de espera:
# -s (silencioso), -f (fallar si hay error HTTP como 404), -o /dev/null (no mostrar la respuesta)
# El bucle se repite mientras 'curl' falle (código de salida != 0)
until curl -s -f -o /dev/null "$KEYCLOAK_URL"
do
  echo "Keycloak o el realm 'MetaMapa' aún no están listos. Reintentando en 15 segundos..."
  sleep 15
done

echo "------ ¡Keycloak y el realm MetaMapa están listos! ------"
echo "------ Iniciando Spring Boot (api-gateway)... ------"

# Ahora que Keycloak está listo, ejecutamos el comando original del Dockerfile
# (El JAR se copió a /app/app.jar)
exec java -jar /app/app.jar