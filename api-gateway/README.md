# API Gateway - Puerta de Entrada Principal

## Descripción

El módulo **API Gateway** es el punto de entrada principal de la plataforma MetaMapa. Actúa como un enrutador central que:

- 🔀 **Enruta solicitudes** hacia los diferentes microservicios (Agregador, Estadísticas, Dinámica)
- 🔐 **Autentica y autoriza** usuarios mediante OAuth2/JWT (Keycloak)
- ⏱️ **Limita el acceso** a través de Rate Limiting por IP
- 🌐 **Gestiona CORS** para permitir solicitudes desde el frontend
- 📊 **Expone métricas** de Prometheus para monitoreo
- 📝 **Permite GraphQL** directo al agregador

## Características Principales

- 🔀 **Enrutamiento Inteligente**: Redirección de solicitudes a microservicios
- 🔐 **Seguridad OAuth2/JWT**: Integración con Keycloak para autenticación
- ⏸️ **Rate Limiting**: Control de tasa de solicitudes por IP (5 req/seg, ráfagas de 10)
- 🌐 **CORS Global**: Configuración centralizada de CORS
- 📊 **Monitoreo**: Métricas de Prometheus y health checks
- 🔑 **Multi-Rol**: Soporte para roles ADMIN y usuarios autenticados
- ♻️ **Reactividad**: Implementación reactiva con WebFlux

## Tecnología

- **Framework**: Spring Boot 3.3.1
- **Cloud**: Spring Cloud Gateway
- **Lenguaje**: Java 17
- **Seguridad**: Spring Security OAuth2 + Keycloak
- **Rate Limiting**: Redis + Spring Cloud Gateway Rate Limiter
- **Monitoreo**: Micrometer + Prometheus
- **Stack Reactivo**: WebFlux, Reactive Redis

## Configuración

### Variables de Entorno Requeridas

```bash
# Servicios Backend
AGREGADOR_URL=http://localhost:8080
ESTADISTICA_URL=http://localhost:8081
DINAMICA_URL=http://localhost:8082

# Frontend
REACT_APP_API_URL=http://localhost:3000

# Keycloak
KEYCLOAK_URL=http://localhost:8180
KEYCLOAK_JWK_SET_URI=http://localhost:8180/realms/MetaMapa/protocol/openid-connect/certs
KEYCLOAK_VALID_ISSUERS=http://localhost:8180/realms/MetaMapa

# Redis para Rate Limiting
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6379
```

### Configuración Principal

- **Puerto**: 8089
- **Rate Limit Agregador**: 5 req/seg, ráfagas de 10
- **Rate Limit Estadísticas**: 5 req/seg, ráfagas de 10
- **Rate Limit Dinámica**: 5 req/seg, ráfagas de 10
- **Logs**: `logs/api-gateway.log`

## Rutas y Enrutamiento

### Agregador (Enrutador 0)
```
ID: agregador
Ruta: /agregador/**
Backend: ${AGREGADOR_URL}
Rate Limit: 5 req/seg, burst 10
```

Todas las solicitudes a `/agregador/*` se enrutan al servicio Agregador.

---

### Estadísticas (Enrutador 1)
```
ID: estadisticas
Ruta: /api/estadisticas/**
Backend: ${ESTADISTICA_URL}
Rate Limit: 5 req/seg, burst 10
```

Todas las solicitudes a `/api/estadisticas/*` se enrutan al servicio Estadísticas.

---

### Dinámica (Enrutador 2)
```
ID: dinamica
Ruta: /api/dinamica/**
Backend: ${DINAMICA_URL}
Rate Limit: 5 req/seg, burst 10
```

Todas las solicitudes a `/api/dinamica/*` se enrutan al servicio Dinámica.

---

### GraphQL (Enrutador 3)
```
ID: agregador-graphql
Ruta: /graphql
Backend: ${AGREGADOR_URL}
```

Solicitudes GraphQL directas al endpoint `/graphql` se enrutan al Agregador.

---

### GraphiQL UI (Enrutador 4)
```
ID: agregador-graphiql
Ruta: /graphiql/**
Backend: ${AGREGADOR_URL}
```

La interfaz GraphQL (`/graphiql/*`) se enruta al Agregador.

---

## Seguridad y Autorizacion

### Niveles de Acceso

#### 1. **Público (sin autenticación)**
- `GET /agregador/colecciones`
- `GET /agregador/colecciones/{id}`
- `GET /agregador/colecciones/{id}/hechos`
- `GET /agregador/categorias`
- `GET /agregador/hechos`
- `GET /agregador/hechos/{id}`
- `GET /agregador/search`
- `GET /graphql` (consultas)
- `POST /graphql` (consultas)
- `GET /graphiql/**`
- `POST /api/dinamica/hechos`
- `POST /api/dinamica/upload/{id}`
- `POST /agregador/solicitudes`
- Health check: `/actuator/health`
- Métricas: `/actuator/prometheus`

#### 2. **Rol ADMIN (autenticado + rol ADMIN)**
- `POST /agregador/colecciones` - Crear colecciones
- `PATCH /agregador/colecciones/{id}` - Modificar colecciones
- `DELETE /agregador/colecciones/{id}` - Eliminar colecciones
- `GET /agregador/solicitudes` - Ver todas las solicitudes
- `GET /agregador/solicitudes/pendientes` - Ver solicitudes pendientes
- `PUT /agregador/solicitudes/{id}` - Aceptar/rechazar solicitudes
- `GET /api/estadisticas/categorias`
- `GET /api/estadisticas/colecciones/provincia-max-hechos`
- `GET /api/estadisticas/hechos/max-categoria`
- `GET /api/estadisticas/categoria/provincia-max-hechos`
- `GET /api/estadisticas/categoria/hora`
- `GET /api/estadisticas/solicitudes/spam`

#### 3. **Autenticado (solo token válido)**
- `POST /agregador/solicitudes` - Crear solicitudes propias
- `/**` - Acceso general a rutas protegidas

---

## Manejo de CORS

### Origen Permitido
- `${visualizador.url}` (por defecto: http://localhost:3000)

### Métodos Permitidos
- `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`

### Headers Permitidos
- `Authorization`
- `Content-Type`
- Todos los demás (`*`)

### Credenciales
- `allow-credentials: true`

---

## Rate Limiting

### Configuración Global
- **Tipo**: Por IP del cliente
- **Algoritmo**: Token Bucket (Redis)
- **Resolución de clave**: Dirección IP remota del cliente

### Límites por Ruta
```
Agregador (/agregador/**):
  - ReplenishRate: 5 requests/segundo
  - BurstCapacity: 10 requests simultáneos

Estadísticas (/api/estadisticas/**):
  - ReplenishRate: 5 requests/segundo
  - BurstCapacity: 10 requests simultáneos

Dinámica (/api/dinamica/**):
  - ReplenishRate: 5 requests/segundo
  - BurstCapacity: 10 requests simultáneos
```

### Ejemplo de Límite Superado
```
HTTP 429 Too Many Requests
```

---

## Autenticación OAuth2/JWT

### Flujo de Autenticación

1. **Usuario solicita token** en Keycloak
2. **Keycloak emite JWT** firmado
3. **Cliente envía Authorization header** con el token
4. **API Gateway valida el token**:
   - Verifica firma usando JWKS de Keycloak
   - Valida timestamp (expiración)
   - Valida issuer (emisor)
5. **Si es válido**, se extrae información del usuario y se enruta la solicitud
6. **Si es inválido**, se devuelve error 401 Unauthorized

### Validación de Issuers

El gateway acepta múltiples issuers válidos configurados en `KEYCLOAK_VALID_ISSUERS`:
```
http://localhost:8180/realms/MetaMapa
https://keycloak.midominio.com/realms/MetaMapa
```

### Extracción de Roles

Los roles se extraen de la sección `realm_access.roles` del JWT:
```json
{
  "realm_access": {
    "roles": ["ADMIN", "user"]
  }
}
```

### Ejemplo de Request Autenticado
```bash
curl -X GET http://localhost:8089/agregador/solicitudes \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Monitoreo y Métricas

### Health Check
```
GET /actuator/health
```

**Respuesta**:
```json
{
  "status": "UP",
  "components": {
    "diskSpace": { "status": "UP" },
    "db": { "status": "UP" }
  }
}
```

---

### Métricas de Prometheus
```
GET /actuator/prometheus
```

Devuelve métricas en formato Prometheus incluyendo:
- Latencia de solicitudes
- Número de solicitudes totales
- Tasa de errores
- Rate limiting metrics
- Información de la aplicación

---

### Información de Aplicación
```
GET /actuator/info
```

**Respuesta**:
```json
{
  "app": {
    "name": "api-gateway",
    "description": "API Gateway",
    "version": "0.0.1-SNAPSHOT"
  }
}
```

---

## Ejemplos de Uso

### Ejemplo 1: Consulta Pública (Sin autenticación)
```bash
# Obtener colecciones públicas
curl http://localhost:8089/agregador/colecciones

# Buscar hechos
curl "http://localhost:8089/agregador/search?texto=robo"
```

---

### Ejemplo 2: Autenticación y Consulta Protegida
```bash
# 1. Obtener token de Keycloak
TOKEN=$(curl -X POST http://localhost:8180/realms/MetaMapa/protocol/openid-connect/token \
  -d "client_id=metamapa-client" \
  -d "client_secret=your-secret" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" | jq -r '.access_token')

# 2. Usar token para crear colección (requiere ADMIN)
curl -X POST http://localhost:8089/agregador/colecciones \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Nueva Colección",
    "descripcion": "Descripción",
    "algoritmo_consenso": "MAYORIA",
    "urls_fuente": ["http://fuente.com"]
  }'
```

---

### Ejemplo 3: GraphQL a través del Gateway
```bash
# Consulta GraphQL pública (sin token)
curl -X POST http://localhost:8089/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { hechos { id titulo } }"
  }'
```

---

### Ejemplo 4: Rate Limiting en Acción
```bash
# Hacer más de 10 solicitudes rápidas (burst capacity)
for i in {1..15}; do
  curl http://localhost:8089/agregador/colecciones
done

# Las solicitudes después de 10 devuelven:
# HTTP 429 Too Many Requests
```

---

### Ejemplo 5: Estadísticas (Solo ADMIN)
```bash
curl http://localhost:8089/api/estadisticas/solicitudes/spam \
  -H "Authorization: Bearer $TOKEN"
```

---

## Estructura de Configuración

### SecurityConfig.java
- **JwtDecoder**: Configura validación de JWT con múltiples issuers
- **SecurityWebFilterChain**: Define reglas de autorización por endpoint
- **CORS Configuration**: Configuración de CORS reactivo
- **Conversor JWT**: Extrae roles del token JWT

### RateLimiterConfig.java
- **ipKeyResolver**: Resuelve la IP del cliente como clave del rate limiter

### ActuatorSecurityConfig.java
- **actuatorSecurityFilterChain**: Permite acceso público a `/actuator/**`

---

## Resolución de Problemas

### Error 401 Unauthorized
**Causa**: Token inválido, expirado o no enviado
**Solución**: Verificar token en Keycloak, renovar si expiró

### Error 403 Forbidden
**Causa**: Usuario autenticado pero sin rol requerido
**Solución**: Asignar rol ADMIN al usuario en Keycloak

### Error 429 Too Many Requests
**Causa**: Rate limit superado
**Solución**: Esperar a que se reponga la cuota (5 req/seg)

### Error de CORS
**Causa**: Origen (origin) no permitido
**Solución**: Verificar `visualizador.url` en configuración

### No se conecta a Redis
**Causa**: Redis no disponible o credenciales incorrectas
**Solución**: Verificar `SPRING_DATA_REDIS_HOST` y `SPRING_DATA_REDIS_PORT`

---

## Desarrollo y Compilación

### Compilar
```bash
mvn clean install
```

### Ejecutar Localmente
```bash
mvn spring-boot:run
```

### Ejecutar Tests
```bash
mvn test
```

### Generar JAR
```bash
mvn package
```

### Ejecutar JAR
```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

---

## Integración con Keycloak

El gateway requiere que Keycloak esté disponible para:
1. Validar firmas JWT (JWKS endpoint)
2. Extraer roles y permisos
3. Renovar tokens (si es necesario en el cliente)

### Configuración Esperada en Keycloak
- Realm: `MetaMapa`
- Clientes: `metamapa-client`
- Roles: `ADMIN`, `USER`
- Tipo de flujo: OpenID Connect Authorization Code

---

## Integración con Redis

Redis es requerido para:
1. **Rate Limiting**: Almacenar contadores por IP
2. **Token Bucket Algorithm**: Mantener estado del límite

Si Redis no está disponible, el gateway no funcionará correctamente.

---

## Seguridad Recomendada

### En Producción
- ✅ Usar HTTPS en lugar de HTTP
- ✅ Cambiar `visualizador.url` al dominio real
- ✅ Usar Keycloak con certificados válidos
- ✅ Proteger credenciales en secrets/variables de entorno
- ✅ Aumentar Rate Limits según capacidad
- ✅ Monitorear logs y métricas
- ✅ Habilitar CSRF para endpoints sensibles
- ✅ Usar connection pooling en Redis

### En Desarrollo
- ✅ Mismo esquema que producción para testing
- ✅ Usar localhost con configuración clara
- ✅ Documentar credenciales de desarrollo

