#!/bin/bash
set -e

# Usar la variable de entorno que viene de docker-compose
KEYCLOAK_URL=${KEYCLOAK_URL:-"http://keycloak:8080"}
# Eliminar trailing slash si existe
KEYCLOAK_URL="${KEYCLOAK_URL%/}"

REALM="MetaMapa"
ADMIN_USER=${KEYCLOAK_ADMIN:-"admin"}
ADMIN_PASSWORD=${KEYCLOAK_ADMIN_PASSWORD:-"admin"}

until curl -sf "${KEYCLOAK_URL}/realms/master" > /dev/null 2>&1; do
  echo "Waiting for Keycloak... Looking at ${KEYCLOAK_URL}/realms/master"
  sleep 5
done

echo "Keycloak estÃ¡ listo. Iniciando creaciÃ³n de usuarios..."

echo "Obteniendo token de acceso..."
ACCESS_TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=$ADMIN_USER" \
  -d "password=$ADMIN_PASSWORD" \
  -d 'grant_type=password' \
  -d 'client_id=admin-cli' | jq -r '.access_token')

if [ "$ACCESS_TOKEN" == "null" ] || [ -z "$ACCESS_TOKEN" ]; then
  echo "Error: No se pudo obtener el token de acceso."
  exit 1
fi

echo "Token obtenido exitosamente."



create_user() {
  local username=$1
  local password=$2
  local firstname=$3
  local lastname=$4
  local email=$5
  local role=$6

  echo "Verificando si el usuario '$username' ya existe..."
  USER_EXISTS=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/users?username=$username" \
    -H "Authorization: Bearer $ACCESS_TOKEN" \
    -H "Content-Type: application/json" | jq '. | length')

  if [ "$USER_EXISTS" -gt 0 ]; then
    echo "El usuario '$username' ya existe. Omitiendo creaciÃ³n."
    return
  fi

  echo "Creando usuario '$username'..."

  CREATE_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users" \
    -H "Authorization: Bearer $ACCESS_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "username": "'"$username"'",
      "enabled": true,
      "emailVerified": true,
      "firstName": "'"$firstname"'",
      "lastName": "'"$lastname"'",
      "email": "'"$email"'",
      "credentials": [{
        "type": "password",
        "value": "'"$password"'",
        "temporary": false
      }]
    }')

  HTTP_CODE=$(echo "$CREATE_RESPONSE" | tail -n1)

  if [ "$HTTP_CODE" == "201" ]; then
    echo "Usuario '$username' creado exitosamente."

    USER_ID=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/users?username=$username" \
      -H "Authorization: Bearer $ACCESS_TOKEN" \
      -H "Content-Type: application/json" | jq -r '.[0].id')

    ROLE_ID=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/roles/$role" \
      -H "Authorization: Bearer $ACCESS_TOKEN" \
      -H "Content-Type: application/json" | jq -r '.id')

    echo "Asignando rol '$role' al usuario '$username'..."
    curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users/$USER_ID/role-mappings/realm" \
      -H "Authorization: Bearer $ACCESS_TOKEN" \
      -H "Content-Type: application/json" \
      -d '[{
        "id": "'"$ROLE_ID"'",
        "name": "'"$role"'"
      }]'

    echo "Rol '$role' asignado exitosamente a '$username'."
  else
    echo "Error al crear usuario '$username'. CÃ³digo HTTP: $HTTP_CODE"
  fi
}

echo ""
echo "=== Creando usuarios de prueba ==="
echo ""

create_user "admin" "admin" "Admin" "User" "admin@metamapa.com" "admin"
create_user "test123" "test123" "Test" "User" "test123@metamapa.com" "user"

echo ""
echo "=== Proceso completado ==="
echo "Usuarios creados:"
echo "  - admin / admin (rol: admin)"
echo "  - test123 / test123 (rol: user)"

