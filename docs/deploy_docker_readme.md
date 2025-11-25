 Deploy Docker (MetaMapa) – Guía rápida

## 1. Variables y archivos
- `.env` (raíz): credenciales DB, puertos, Keycloak URL, etc.
  - `MYSQL_USER=admin`, `MYSQL_PASSWORD=...`
  - `MYSQL_DB_AGREGADOR/…/DINAMICA/…/ESTADISTICA/GESTOR`
  - `KEYCLOAK_URL=http://keycloak:9090/realms/MetaMapa`
  - `GATEWAY_PORT=8089`, `FRONTEND_PORT=3000`
  - `REACT_APP_API_URL=http://localhost:8089` (para dev; en build se pasa por build-arg)
- `docker-compose.prod.yml`: define todos los servicios.
- `readmes/init-databases.sql`: crea las BD.
- Frontend: `visualizador/Dockerfile` + `visualizador/nginx.conf` (fallback SPA).

## 2. Levantar todo (producción/dev)
```bash
# detener MySQL host si usa 3306
docker compose -f docker-compose.prod.yml down -v
docker compose -f docker-compose.prod.yml up -d --build
docker compose -f docker-compose.prod.yml ps
```
Servicios:
- MySQL (mysql:8.0) con volumen `mysql_data` e init sql.
- Keycloak (9090) con volumen `keycloak_data`.
- Backend: api-gateway (8089), agregador (8080 interno), dinamica (8082), estatica (8081), estadistica (8088), gestor-personas (8091), proxy (8083), proxyEjemplo (8092), normalizador (8087).
- Frontend (nginx): expone `FRONTEND_PORT:80` (por defecto 3000:80).

## 3. MySQL y permisos
Si se recrea el volumen, asegura GRANTs a `admin`:
```bash
docker exec mysql mysql -uroot -p$MYSQL_PASSWORD -e "
CREATE DATABASE IF NOT EXISTS dinamica_db;
CREATE DATABASE IF NOT EXISTS agregador_db;
CREATE DATABASE IF NOT EXISTS estadistica_db;
CREATE DATABASE IF NOT EXISTS usuarios_db;
CREATE DATABASE IF NOT EXISTS gestor_db;
GRANT ALL PRIVILEGES ON dinamica_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON agregador_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON estadistica_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON usuarios_db.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON gestor_db.* TO 'admin'@'%';
FLUSH PRIVILEGES;"
```

## 4. Keycloak
1) Realm: crea “MetaMapa” (no usar master).
2) Cliente frontend (public):
   - Client ID: `metamapa-frontend`
   - Valid redirect URIs: `http://localhost:3000/*`
   - Web origins: `http://localhost:3000` (en pruebas, `+` para permitir todos).
   - Standard flow: ON. Client authentication: OFF (si es public).
3) Usuario de prueba con contraseña y roles si aplica.
4) Gateway usa `KEYCLOAK_URL=http://keycloak:9090/realms/MetaMapa`.

Token rápido (grant password):
```bash
curl -X POST "http://localhost:9090/realms/MetaMapa/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=metamapa-frontend" \
  -d "username=<user>" \
  -d "password=<pass>" \
  -d "grant_type=password"
```

## 5. Gateway y seguridad
- Actualmente el gateway exige JWT salvo rutas marcadas `permitAll`. Ajusta `SecurityConfig` si quieres todo público.
- Frontend llama al gateway (8089). Si no hay token y la ruta está protegida, obtendrás 401.

## 6. Frontend (nginx)
- `visualizador/nginx.conf` tiene fallback SPA: `try_files $uri /index.html;` para evitar 404 en rutas como `/home`.
- Build con API interna:
  ```bash
  docker compose -f docker-compose.prod.yml build frontend \
    --build-arg REACT_APP_API_URL=http://api-gateway:8089
  ```

## 7. Servicios y datos
- Estatica carga CSV desde `estatica/src/main/resources/archivos` al iniciar (ver logs de estatica para contar hechos).
- Dinamica persiste hechos en `dinamica_db` (requiere GRANT correcto). Los hechos reportados se guardan en dinamica y luego el agregador los consulta vía HTTP al servicio dinamica.
- Agregador persiste su vista en `agregador_db` y combina estatica/dinamica/proxy; si dinamica no arranca, las consultas fallan.

## 8. Comandos útiles
- Ver estado: `docker compose -f docker-compose.prod.yml ps`
- Logs servicio: `docker compose -f docker-compose.prod.yml logs <servicio>`
- Reiniciar servicio: `docker compose -f docker-compose.prod.yml restart <servicio>`

## 9. Errores típicos y cómo resolverlos
- Dinámica reinicia / agregador da 500 (UnknownHost/Connection refused): permisos de MySQL faltantes. Ejecuta los GRANT con la clave real de root: `Get-Content docs/mysql_grants.sql | docker exec -i mysql mysql -uroot -proot` y `docker compose restart dinamica agregador estadistica gestor-personas`.
- Estática devuelve 400 en `/api/estatica/hechos`: el endpoint exige el query `archivosProcesados`; usa `?archivosProcesados=` (vacío) para importar todos, o pasa la lista de CSV.
- No conecta a Estática/Dinámica desde el host: estos servicios no exponen puertos al host; prueba vía gateway (8089) o desde dentro de la red con `docker compose exec <svc> curl http://<svc>:<port>/...`.
- Keycloak se “pierde”: si haces `docker compose down -v` se borra el volumen y hay que recrear realm/cliente. Evita `-v` salvo que quieras reconfigurar todo.
- Agregador 500 en categorías/ubicaciones: suele ser cascada de lo anterior (Dinámica caída o Estática devolviendo 400). Asegura que ambas respondan y que Estática se llame con el parámetro adecuado.
