#!/bin/bash

echo "Esperando a que MySQL esté disponible..."

until mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SELECT 1" > /dev/null 2>&1; do
  echo "MySQL no está disponible aún. Reintentando en 5 segundos..."
  sleep 5
done

echo "MySQL está disponible. Creando bases de datos..."

mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<EOF
CREATE DATABASE IF NOT EXISTS keycloak_db;
CREATE DATABASE IF NOT EXISTS agregador_db;
CREATE DATABASE IF NOT EXISTS dinamica_db;
CREATE DATABASE IF NOT EXISTS estadistica_db;
CREATE DATABASE IF NOT EXISTS gestor_db;

SHOW DATABASES;
EOF

echo "Bases de datos creadas exitosamente!"
echo "Listado de bases de datos:"

mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SHOW DATABASES;"

echo "Script completado. Este contenedor puede detenerse ahora."
