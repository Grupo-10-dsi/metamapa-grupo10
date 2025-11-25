# init-databases.sql

# Crea las bases de datos si no existen
CREATE DATABASE IF NOT EXISTS dinamica_db;
CREATE DATABASE IF NOT EXISTS agregador_db;
CREATE DATABASE IF NOT EXISTS estadistica_db;
CREATE DATABASE IF NOT EXISTS usuarios_db;
CREATE DATABASE IF NOT EXISTS gestor_db;

# Otorga privilegios al usuario 'admin' (definido en tu .env como MYSQL_USER)
# Asegúrate de que el usuario 'admin' se cree con la contraseña definida en .env
# Nota: La imagen de MySQL 8.0 crea automáticamente el usuario definido en MYSQL_USER
# y le da permisos sobre la base definida en MYSQL_DATABASE.
# Estos GRANTs aseguran que tenga permisos sobre TODAS las bases.

GRANT ALL PRIVILEGES ON dinamica_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON agregador_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON estadistica_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON usuarios_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON gestor_db.* TO 'admin'@'%';

CREATE DATABASE IF NOT EXISTS keycloak_db;
GRANT ALL PRIVILEGES ON keycloak_db.* TO 'admin'@'%';

# Aplica los cambios de privilegios
FLUSH PRIVILEGES;