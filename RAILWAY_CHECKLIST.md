# ✅ Checklist de Deployment en Railway

Usa este checklist para asegurarte de que todos los pasos están completos.

## Pre-Deployment

- [ ] Repositorio subido a GitHub
- [ ] Archivos `railway.json` en la raíz y servicios específicos
- [ ] Archivo `.env` NO está en el repositorio (debe estar en `.gitignore`)
- [ ] Dockerfile en cada servicio funciona localmente

## Configuración en Railway

### 1. Proyecto Base
- [ ] Proyecto creado en Railway
- [ ] MySQL agregado al proyecto
- [ ] Bases de datos adicionales creadas en MySQL:
  - [ ] `agregador_db`
  - [ ] `dinamica_db`
  - [ ] `estadistica_db`
  - [ ] `gestor_db`
  - [ ] `keycloak_db`

### 2. Keycloak
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `keycloak-config`
- [ ] Railway detectará automáticamente el Dockerfile
- [ ] Variables de entorno configuradas:
  - [ ] `KC_DB`
  - [ ] `KC_DB_URL_HOST`
  - [ ] `KC_DB_URL_PORT`
  - [ ] `KC_DB_URL_DATABASE`
  - [ ] `KC_DB_USERNAME`
  - [ ] `KC_DB_PASSWORD`
  - [ ] `KC_BOOTSTRAP_ADMIN_USERNAME`
  - [ ] `KC_BOOTSTRAP_ADMIN_PASSWORD`
  - [ ] `KC_HTTP_PORT`
  - [ ] `KC_HTTP_ENABLED`
  - [ ] `KC_HOSTNAME_STRICT`
  - [ ] `KC_PROXY_HEADERS`
- [ ] Dominio público generado
- [ ] Servicio desplegado exitosamente
- [ ] Accesible en: `https://<keycloak-domain>`

### 3. Servicios Backend

#### Estatica
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `estatica`
- [ ] Build exitoso
- [ ] Servicio running

#### Dinamica
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `dinamica`
- [ ] Variables configuradas:
  - [ ] `SPRING_DATASOURCE_URL`
  - [ ] `SPRING_DATASOURCE_USERNAME`
  - [ ] `SPRING_DATASOURCE_PASSWORD`
  - [ ] `GESTOR_PERSONAS_URL`
  - [ ] `SUPABASE_URL`
  - [ ] `SUPABASE_BUCKET`
  - [ ] `SUPABASE_SERVICE_KEY`
- [ ] Build exitoso
- [ ] Servicio running

#### Normalizador
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `normalizador`
- [ ] Build exitoso
- [ ] Servicio running

#### ProxyEjemplo
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `proxyEjemplo`
- [ ] Build exitoso
- [ ] Servicio running

#### Proxy
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `proxy`
- [ ] Variables configuradas:
  - [ ] `METAMAPA_INSTANCIA_URL`
- [ ] Build exitoso
- [ ] Servicio running

#### Gestor-Personas
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `gestor-personas`
- [ ] Variables configuradas:
  - [ ] `SPRING_DATASOURCE_URL`
  - [ ] `SPRING_DATASOURCE_USERNAME`
  - [ ] `SPRING_DATASOURCE_PASSWORD`
- [ ] Build exitoso
- [ ] Servicio running

#### Agregador
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `agregador`
- [ ] Variables configuradas:
  - [ ] `SPRING_DATASOURCE_URL`
  - [ ] `SPRING_DATASOURCE_USERNAME`
  - [ ] `SPRING_DATASOURCE_PASSWORD`
  - [ ] `ESTATICA_URL`
  - [ ] `DINAMICA_URL`
  - [ ] `PROXY_URL`
  - [ ] `NORMALIZADOR_URL`
- [ ] Build exitoso
- [ ] Servicio running

#### Estadistica
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `estadistica`
- [ ] Variables configuradas:
  - [ ] `SPRING_DATASOURCE_URL`
  - [ ] `SPRING_DATASOURCE_USERNAME`
  - [ ] `SPRING_DATASOURCE_PASSWORD`
  - [ ] `AGREGADOR_URL`
- [ ] Build exitoso
- [ ] Servicio running

### 4. API Gateway
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `api-gateway`
- [ ] Variables configuradas:
  - [ ] `KEYCLOAK_URL`
  - [ ] `KEYCLOAK_JWK_SET_URI`
  - [ ] `KEYCLOAK_VALID_ISSUERS`
  - [ ] `ESTADISTICA_URL`
  - [ ] `DINAMICA_URL`
  - [ ] `AGREGADOR_URL`
- [ ] Dominio público generado
- [ ] Build exitoso
- [ ] Servicio running
- [ ] Health check OK: `https://<api-gateway-domain>/actuator/health`

### 5. Frontend (Visualizador)
- [ ] Servicio creado desde GitHub
- [ ] Root Directory: `visualizador`
- [ ] Variables configuradas:
  - [ ] `REACT_APP_API_GATEWAY_URL_BASE`
  - [ ] `REACT_APP_KEYCLOAK_CLIENT_ID`
- [ ] Dominio público generado
- [ ] Build exitoso
- [ ] Servicio running
- [ ] Accesible en: `https://<frontend-domain>`

## Post-Deployment

### Configuración de Keycloak
- [ ] Accedido a consola de administración de Keycloak
- [ ] Login con `admin` / `admin`
- [ ] Realm "MetaMapa" importado o creado
- [ ] Cliente "metamapa-frontend" configurado:
  - [ ] Valid Redirect URIs incluye frontend domain
  - [ ] Web Origins incluye frontend domain
- [ ] Roles creados:
  - [ ] `admin`
  - [ ] `user`
- [ ] Usuarios de prueba creados:
  - [ ] Usuario admin con rol admin
  - [ ] Usuario test con rol user

### Testing End-to-End
- [ ] Frontend carga correctamente
- [ ] Login con Keycloak funciona
- [ ] Redirección después de login funciona
- [ ] API Gateway acepta requests autenticados
- [ ] Servicios backend responden correctamente
- [ ] CRUD operations funcionan
- [ ] Logout funciona

## Monitoreo

- [ ] Logs revisados para cada servicio
- [ ] No hay errores críticos en logs
- [ ] Métricas de recursos están normales (CPU, memoria)
- [ ] Networking privado funciona entre servicios
- [ ] URLs públicas son accesibles

## Seguridad

- [ ] Credenciales de Keycloak admin cambiadas (producción)
- [ ] Variables de entorno con secrets no están en Git
- [ ] HTTPS habilitado en todos los servicios públicos
- [ ] CORS configurado correctamente en API Gateway
- [ ] MySQL solo accesible desde red privada de Railway

## Documentación

- [ ] URLs públicas documentadas
- [ ] Credenciales de acceso guardadas en lugar seguro
- [ ] Diagrama de arquitectura actualizado
- [ ] README actualizado con instrucciones de deployment

## Opcional

- [ ] Custom domains configurados
- [ ] Monitoring/alerting configurado
- [ ] Backups de base de datos configurados
- [ ] CI/CD pipeline configurado (GitHub Actions)
- [ ] Health checks configurados
- [ ] Rate limiting configurado

---

## URLs de Referencia

Anota aquí tus URLs después del deployment:

- **Frontend**: `https://________________________`
- **API Gateway**: `https://________________________`
- **Keycloak**: `https://________________________`
- **Railway Dashboard**: `https://railway.app/project/________________________`

---

## Notas

Escribe aquí cualquier nota importante sobre el deployment:

```
[Tu notas aquí]
```
