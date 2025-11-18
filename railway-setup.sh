#!/bin/bash

# Script de ayuda para configurar servicios en Railway
# Este script muestra las variables de entorno que necesitas configurar para cada servicio

echo "========================================="
echo "RAILWAY - Configuración de Variables"
echo "========================================="
echo ""
echo "Este script te guiará en la configuración de variables de entorno para cada servicio."
echo ""

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}PASO 1: Crear Proyecto en Railway${NC}"
echo "1. Ve a https://railway.app/dashboard"
echo "2. Click en 'New Project' → 'Empty Project'"
echo ""

echo -e "${BLUE}PASO 2: Agregar MySQL${NC}"
echo "1. Click en '+ New' → 'Database' → 'Add MySQL'"
echo "2. Railway creará automáticamente las variables de MySQL"
echo ""

echo -e "${BLUE}PASO 3: Crear bases de datos adicionales${NC}"
echo "Ejecuta estos comandos en MySQL (desde Railway Dashboard):"
echo ""
echo -e "${GREEN}CREATE DATABASE IF NOT EXISTS agregador_db;${NC}"
echo -e "${GREEN}CREATE DATABASE IF NOT EXISTS dinamica_db;${NC}"
echo -e "${GREEN}CREATE DATABASE IF NOT EXISTS estadistica_db;${NC}"
echo -e "${GREEN}CREATE DATABASE IF NOT EXISTS gestor_db;${NC}"
echo -e "${GREEN}CREATE DATABASE IF NOT EXISTS keycloak_db;${NC}"
echo ""

echo -e "${BLUE}PASO 4: Agregar Keycloak${NC}"
echo "1. Click en '+ New' → 'Empty Service'"
echo "2. Nombre: 'keycloak'"
echo "3. En Settings → Source:"
echo "   - Source: Docker Image"
echo "   - Image: quay.io/keycloak/keycloak:26.4.2"
echo "   - Deploy Command: start --import-realm"
echo ""
echo "4. Variables de entorno para keycloak:"
echo ""
cat << 'EOF'
KC_DB=mysql
KC_DB_URL_HOST=${{MySQL.MYSQLHOST}}
KC_DB_URL_PORT=${{MySQL.MYSQLPORT}}
KC_DB_URL_DATABASE=keycloak_db
KC_DB_USERNAME=${{MySQL.MYSQLUSER}}
KC_DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
KC_BOOTSTRAP_ADMIN_USERNAME=admin
KC_BOOTSTRAP_ADMIN_PASSWORD=admin
KC_HTTP_PORT=8080
KC_HTTP_ENABLED=true
KC_HOSTNAME_STRICT=false
KC_PROXY_HEADERS=xforwarded
EOF
echo ""

echo -e "${BLUE}PASO 5: Agregar servicios desde GitHub${NC}"
echo ""
echo "Para cada servicio, haz lo siguiente:"
echo "1. Click en '+ New' → 'GitHub Repo'"
echo "2. Selecciona tu repositorio"
echo "3. Configura Root Directory y Variables"
echo ""

echo -e "${YELLOW}Lista de servicios a crear:${NC}"
services=(
  "estatica"
  "dinamica"
  "normalizador"
  "proxyEjemplo"
  "proxy"
  "gestor-personas"
  "agregador"
  "estadistica"
  "api-gateway"
  "visualizador"
)

for service in "${services[@]}"; do
  echo "  - $service"
done
echo ""

echo -e "${BLUE}PASO 6: Variables compartidas${NC}"
echo "Agrega estas variables a nivel de proyecto (Shared Variables):"
echo ""
cat << 'EOF'
MYSQL_DB_AGREGADOR=agregador_db
MYSQL_DB_DINAMICA=dinamica_db
MYSQL_DB_ESTADISTICA=estadistica_db
MYSQL_DB_GESTOR=gestor_db
SUPABASE_URL=https://ycewwqszmnadqvimhdpx.supabase.co/
SUPABASE_BUCKET=multimedia
SUPABASE_SERVICE_KEY=<tu-clave>
EOF
echo ""

echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}Para más detalles, consulta RAILWAY.md${NC}"
echo -e "${GREEN}=========================================${NC}"
