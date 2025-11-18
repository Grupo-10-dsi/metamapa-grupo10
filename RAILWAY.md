# Despliegue en Railway

Guía completa para desplegar MetaMapa en Railway.

## 📋 Estructura del Proyecto

El proyecto MetaMapa consta de los siguientes servicios:

- **MySQL**: Base de datos (servicio gestionado de Railway)
- **Keycloak**: Autenticación y autorización
- **API Gateway**: Puerta de entrada principal
- **Frontend (visualizador)**: Interfaz de usuario React
- **Microservicios backend**:
  - agregador
  - dinamica
  - estatica
  - estadistica
  - gestor-personas
  - normalizador
  - proxy
  - proxyEjemplo

## 🚀 Pasos para Desplegar

### 1. Preparar el Repositorio

Asegúrate de que estos archivos estén en tu repositorio:
- `railway.json` en la raíz
- `railway.json` en cada carpeta de servicio (api-gateway, visualizador)
- Dockerfiles en cada servicio

### 2. Crear Proyecto en Railway

1. Ve a [Railway Dashboard](https://railway.app/dashboard)
2. Click en **"New Project"**
3. Selecciona **"Empty Project"**

### 3. Agregar Base de Datos MySQL

1. En tu proyecto vacío, click en **"+ New"**
2. Selecciona **"Database"** → **"Add MySQL"**
3. Railway creará automáticamente el servicio MySQL con estas variables:
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
   - `MYSQLDATABASE`

4. **IMPORTANTE**: Crear las bases de datos adicionales:
   - Entra a MySQL desde Railway CLI o Railway Dashboard
   - Ejecuta:
     ```sql
     CREATE DATABASE IF NOT EXISTS agregador_db;
     CREATE DATABASE IF NOT EXISTS dinamica_db;
     CREATE DATABASE IF NOT EXISTS estadistica_db;
     CREATE DATABASE IF NOT EXISTS gestor_db;
     CREATE DATABASE IF NOT EXISTS keycloak_db;
     ```

### 4. Agregar Servicios desde GitHub

Para cada servicio, haz lo siguiente:

#### A. Keycloak

1. Click **"+ New"** → **"GitHub Repo"**
2. Selecciona tu repositorio `metamapa-grupo10`
3. En **Settings**:
   - **Root Directory**: `keycloak-config`
   - Railway detectará automáticamente el Dockerfile
4. En **Variables**, agrega:
   ```
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
   ```
5. En **Settings** → **Networking**:
   - Habilita **"Generate Domain"** para obtener URL pública
   - Railway asignará automáticamente networking privado

#### B. Servicios desde GitHub (estatica, dinamica, normalizador, etc.)

Para cada servicio backend:

1. Click **"+ New"** → **"GitHub Repo"**
2. Selecciona tu repositorio `metamapa-grupo10`
3. En **Settings**:
   - **Root Directory**: Especifica la carpeta (ej: `estatica`, `dinamica`, etc.)
   - **Build Command**: Railway lo detecta automáticamente desde el Dockerfile
4. En **Variables**, agrega las necesarias (ver sección de variables abajo)

#### C. API Gateway

1. Click **"+ New"** → **"GitHub Repo"**
2. Selecciona tu repositorio
3. En **Settings**:
   - **Root Directory**: `api-gateway`
4. En **Variables**:
   ```
   ESTADISTICA_URL=http://estadistica.railway.internal:8088
   DINAMICA_URL=http://dinamica.railway.internal:8082
   AGREGADOR_URL=http://agregador.railway.internal:8080
   KEYCLOAK_URL=http://keycloak.railway.internal:8080/realms/MetaMapa
   KEYCLOAK_JWK_SET_URI=http://keycloak.railway.internal:8080/realms/MetaMapa/protocol/openid-connect/certs
   KEYCLOAK_VALID_ISSUERS=https://${{keycloak.RAILWAY_PUBLIC_DOMAIN}}/realms/MetaMapa,http://keycloak.railway.internal:8080/realms/MetaMapa
   ```

#### D. Frontend (Visualizador)

1. Click **"+ New"** → **"GitHub Repo"**
2. Selecciona tu repositorio
3. En **Settings**:
   - **Root Directory**: `visualizador`
4. En **Variables**:
   ```
   REACT_APP_API_GATEWAY_URL_BASE=https://${{api-gateway.RAILWAY_PUBLIC_DOMAIN}}
   ```
5. En **Settings** → **Networking**:
   - Habilita **"Generate Domain"** (esta será tu URL pública principal)

### 5. Variables de Entorno Compartidas

Railway permite crear variables compartidas. Agrega estas a nivel de proyecto:

```bash
# MySQL Databases
MYSQL_DB_AGREGADOR=agregador_db
MYSQL_DB_DINAMICA=dinamica_db
MYSQL_DB_ESTADISTICA=estadistica_db
MYSQL_DB_GESTOR=gestor_db

# Supabase
SUPABASE_URL=https://ycewwqszmnadqvimhdpx.supabase.co/
SUPABASE_BUCKET=multimedia
SUPABASE_SERVICE_KEY=<tu-clave-aqui>
```

### 6. Networking Privado en Railway

Railway proporciona networking privado automático entre servicios:

**Formato de URLs internas:**
```
http://<nombre-servicio>.railway.internal:<puerto>
```

**Ejemplos:**
- Keycloak: `http://keycloak.railway.internal:8080`
- Agregador: `http://agregador.railway.internal:8080`
- Dinamica: `http://dinamica.railway.internal:8082`

**URLs públicas:**
Railway asigna automáticamente dominios públicos. Usa la variable:
```
https://${{nombre-servicio.RAILWAY_PUBLIC_DOMAIN}}
```

### 7. Orden de Despliegue Recomendado

1. ✅ MySQL
2. ✅ Keycloak
3. ✅ Servicios base (estatica, normalizador, proxyEjemplo)
4. ✅ Servicios intermedios (dinamica, proxy, gestor-personas)
5. ✅ Agregador
6. ✅ Estadistica
7. ✅ API Gateway
8. ✅ Frontend

### 8. Configuración de Keycloak Post-Deployment

Después de que Keycloak esté desplegado:

1. Accede a la URL pública de Keycloak
2. Login con `admin` / `admin`
3. **Importar Realm**:
   - Ve a "Add Realm" → "Import"
   - Sube el archivo `keycloak-config/MetaMapa.json`

   **O** configura manualmente:
   - Crea realm "MetaMapa"
   - Configura cliente "metamapa-frontend"
   - Crea roles: `admin`, `user`
   - Crea usuarios de prueba

**Alternativamente**, puedes ejecutar el script de inicialización:
```bash
# Desde tu máquina local, apuntando a Railway
bash keycloak-config/init-users.sh
```
(Modifica la URL en el script para apuntar a tu Keycloak en Railway)

### 9. Verificación

Verifica que cada servicio esté corriendo:

```bash
# Instala Railway CLI
npm install -g @railway/cli

# Login
railway login

# Link a tu proyecto
railway link

# Ver logs de un servicio
railway logs <nombre-servicio>

# Ejemplo
railway logs keycloak
railway logs api-gateway
```

### 10. Testing

1. **Keycloak**: Accede a `https://<keycloak-domain>/realms/MetaMapa`
2. **API Gateway**: Accede a `https://<api-gateway-domain>/actuator/health`
3. **Frontend**: Accede a `https://<frontend-domain>`

## 🔧 Troubleshooting

### Keycloak no inicia

**Problema**: Error de conexión a base de datos

**Solución**:
- Verifica que las variables `KC_DB_*` estén correctamente configuradas
- Asegúrate que la base de datos `keycloak_db` exista
- Revisa logs: `railway logs keycloak`

### API Gateway no puede conectar con otros servicios

**Problema**: Connection refused

**Solución**:
- Usa URLs internas: `http://<servicio>.railway.internal:<puerto>`
- NO uses `localhost` o `127.0.0.1`
- Verifica que los servicios dependientes estén running

### Frontend no carga

**Problema**: CORS o API no responde

**Solución**:
- Verifica `REACT_APP_API_GATEWAY_URL_BASE` apunte a la URL pública correcta
- Configura CORS en API Gateway para permitir el dominio del frontend

### Servicio crashea constantemente

**Problema**: OOM (Out of Memory) o recursos insuficientes

**Solución**:
- Aumenta recursos en Railway Settings
- Verifica logs para identificar el error específico
- Considera optimizar la aplicación (reducir uso de memoria)

## 📊 Monitoreo

Railway proporciona:
- **Métricas en tiempo real**: CPU, memoria, network
- **Logs agregados**: Ver logs de todos los servicios
- **Alertas**: Notificaciones de crashes
- **Deployments history**: Rollback si es necesario

## 💰 Costos

Railway ofrece:
- **Free tier**: $5 de créditos mensuales
- **Starter**: $20/mes (más créditos incluidos)
- **Uso adicional**: Pay-as-you-go

**Estimación para este proyecto**:
- ~11 servicios pequeños + 1 MySQL
- Costo aproximado: $10-30/mes (depende del tráfico)

## 🔐 Seguridad

**Recomendaciones**:
1. Cambia las credenciales por defecto de Keycloak
2. Usa variables de entorno para secrets (NO las comitees en Git)
3. Habilita HTTPS en todos los servicios públicos (Railway lo hace automáticamente)
4. Restringe acceso a MySQL (solo servicios internos)

## 📚 Referencias

- [Railway Docs](https://docs.railway.app)
- [Railway Private Networking](https://docs.railway.app/reference/private-networking)
- [Railway Variables](https://docs.railway.app/develop/variables)
- [Keycloak on Railway](https://railway.app/template/keycloak)
