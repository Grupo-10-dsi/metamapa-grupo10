# Gestor de Personas - Módulo de Gestión de Usuarios

## Descripción

El módulo **Gestor de Personas** es responsable de la gestión de usuarios en la plataforma MetaMapa. Actúa como intermediario entre el sistema de autenticación (Keycloak) y la aplicación, sincronizando información de usuarios.

Proporciona un servicio interno para:

- 👤 **Sincronizar usuarios** desde Keycloak a la base de datos local
- 🔐 **Mapear identidades** entre el sistema de autenticación y la aplicación
- 📝 **Almacenar información de usuarios** de forma persistente
- 🔍 **Buscar o crear usuarios** automáticamente
- 📊 **Registrar contribuyentes** para rastrear hechos reportados

## Características Principales

- 🔄 **Sincronización Automática**: Crea usuarios si no existen
- 🔐 **Integración Keycloak**: Lee datos de UUID (sub), email y nombre
- 💾 **Persistencia**: Base de datos MySQL para usuarios
- ⚡ **Transaccional**: Operaciones ACID garantizadas
- 🔑 **Identificación Única**: Usa UUID de Keycloak como clave
- 📊 **Integración Internal**: Servicio llamado solo por otros módulos internos
- 🏷️ **Mapeo de Identidades**: Crea "contribuyentes" identificados

## Tecnología

- **Framework**: Spring Boot 3.5.7
- **Lenguaje**: Java 17
- **Base de Datos**: MySQL
- **ORM**: Spring Data JPA (Hibernate)
- **Autenticación**: Keycloak (integración)
- **API**: REST (uso interno)
- **Monitoreo**: Micrometer + Prometheus

## Configuración

### Variables de Entorno Requeridas

```bash
# Base de datos
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/gestor_personas
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
```

### Configuración Principal

- **Puerto**: 8091
- **Ruta Base**: `/internal/usuarios`
- **DDL Strategy**: update (actualiza esquema automáticamente)
- **Logs**: `/var/log/gestor-personas/`

---

## Arquitectura de Usuario

### Entidad Usuario (Base de Datos)

```java
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                    // Autoincremental (int)
    
    @Column(unique = true, nullable = false, length = 36)
    private String keycloakSub;            // UUID de Keycloak (único)
    
    @Column(nullable = false)
    private String email;                  // Email del usuario
    
    @Column(nullable = false)
    private String nombre;                 // Nombre completo
}
```

**Tabla en Base de Datos**:
```sql
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    keycloak_sub VARCHAR(36) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL
);
```

---

## Endpoints REST API

### Único Endpoint: Find or Create Usuario

```
POST /internal/usuarios/find-or-create
```

**Descripción**: Busca un usuario existente por su Keycloak UUID. Si no existe, lo crea automáticamente en la base de datos.

**Tipo de Solicitud**: Internal (llamado solo por otros microservicios)

**Request Body** (KeycloakUserDTO):
```json
{
  "sub": "123e4567-e89b-12d3-a456-426614174000",
  "email": "usuario@example.com",
  "nombre": "Juan Pérez"
}
```

**Parámetros**:
- `sub` (String, obligatorio): UUID único del usuario en Keycloak
- `email` (String, obligatorio): Email del usuario
- `nombre` (String, obligatorio): Nombre completo del usuario

**Response**: 
- Status: 200 OK
- Body: `UsuarioDTO` con la información de usuario

**Respuesta exitosa**:
```json
{
  "contribuyente_id": 1,
  "contribuyente_nombre": "Juan Pérez"
}
```

**Comportamiento**:

1. **Si el usuario existe** (búsqueda por `keycloakSub`):
   - Devuelve su información actual
   - No actualiza datos

2. **Si el usuario NO existe**:
   - Crea nuevo registro en base de datos
   - Asigna ID autoincremental
   - Devuelve información del nuevo usuario

---

## Modelos de Datos

### KeycloakUserDTO (Request)

DTO que recibe información del usuario desde Keycloak:

```java
public class KeycloakUserDTO {
    private String nombre;        // Nombre del usuario
    private String sub;           // UUID único de Keycloak
    private String email;         // Email del usuario
}
```

**Ejemplo JSON**:
```json
{
  "nombre": "María García",
  "sub": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "email": "maria@example.com"
}
```

---

### Usuario (Entidad)

Entidad JPA que representa un usuario en la base de datos:

```java
@Entity
@Table(name = "usuario")
public class Usuario {
    private Integer id;
    private String keycloakSub;
    private String email;
    private String nombre;
    
    // Método para convertir a DTO
    public UsuarioDTO toDTO() {
        return new UsuarioDTO(this.id, this.nombre);
    }
}
```

---

### UsuarioDTO (Response)

DTO que se devuelve al cliente:

```java
public class UsuarioDTO {
    private Integer contribuyente_id;      // ID de la BD (int, autoincremental)
    private String contribuyente_nombre;   // Nombre del usuario
}
```

**Ejemplo JSON**:
```json
{
  "contribuyente_id": 5,
  "contribuyente_nombre": "María García"
}
```

---

## Ejemplo de Uso

### Escenario 1: Primer usuario (no existe)

**Request**:
```bash
curl -X POST http://localhost:8091/internal/usuarios/find-or-create \
  -H "Content-Type: application/json" \
  -d '{
    "sub": "550e8400-e29b-41d4-a716-446655440000",
    "email": "nuevo@example.com",
    "nombre": "Pedro López"
  }'
```

**Proceso**:
1. Controller recibe request
2. Service busca por `keycloakSub` = "550e8400-..."
3. No encuentra (Optional.empty())
4. Crea nuevo Usuario con:
   - `id` = 1 (autoincremental)
   - `keycloakSub` = "550e8400-..."
   - `email` = "nuevo@example.com"
   - `nombre` = "Pedro López"
5. Guarda en BD
6. Retorna UsuarioDTO

**Response**:
```json
{
  "contribuyente_id": 1,
  "contribuyente_nombre": "Pedro López"
}
```

**BD después**:
```
usuario table:
| id | keycloak_sub                         | email               | nombre        |
|----|--------------------------------------|---------------------|----------------|
| 1  | 550e8400-e29b-41d4-a716-446655440000 | nuevo@example.com   | Pedro López   |
```

---

### Escenario 2: Usuario existente

**Request** (mismos datos):
```bash
curl -X POST http://localhost:8091/internal/usuarios/find-or-create \
  -H "Content-Type: application/json" \
  -d '{
    "sub": "550e8400-e29b-41d4-a716-446655440000",
    "email": "nuevo@example.com",
    "nombre": "Pedro López"
  }'
```

**Proceso**:
1. Controller recibe request
2. Service busca por `keycloakSub` = "550e8400-..."
3. **SÍ encuentra** en BD (id=1)
4. Retorna Usuario existente (sin actualizar)

**Response** (idéntica):
```json
{
  "contribuyente_id": 1,
  "contribuyente_nombre": "Pedro López"
}
```

---

## Integración con Otros Módulos

### Dinámica
Cuando un usuario crea un hecho:
```
1. Frontend envía JWT de Keycloak
2. Dinámica extrae "sub" del JWT
3. Llama a Gestor-Personas: POST /internal/usuarios/find-or-create
4. Obtiene contribuyente_id
5. Asocia el hecho al contribuyente
```

### Agregador
Consultas de hechos filtrados por contribuyente:
```
1. API Gateway enruta a Agregador
2. Agregador necesita mapear UUID a ID
3. Llama a Gestor-Personas si es necesario
4. Usa ID para filtrar hechos
```

---

## Monitoreo y Logs

### Health Check
```
GET /actuator/health
```

**Respuesta**:
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" }
  }
}
```

---

### Métricas de Prometheus
```
GET /actuator/prometheus
```

Devuelve métricas incluyendo:
- Latencia de operaciones
- Número de usuarios creados/buscados
- Errores de base de datos
- Información de la JVM

---

### Logs

Ubicación: `/var/log/gestor-personas/`

Ejemplos de logs:
```
INFO  - Creating new usuario with keycloakSub: 550e8400-...
INFO  - Found existing usuario with id: 1
ERROR - Database connection failed
```

---

## Resolución de Problemas

### Error: Duplicate entry for 'keycloak_sub'
**Causa**: Intento de crear dos usuarios con el mismo UUID de Keycloak
**Solución**: Verificar que el UUID es único en Keycloak

### Error: "Cannot get a connection"
**Causa**: Base de datos no disponible
**Solución**: Verificar credenciales y disponibilidad de MySQL

### Error 400: Bad Request
**Causa**: Campos faltantes en el request
**Solución**: Verificar que KeycloakUserDTO incluya `sub`, `email` y `nombre`

---

## Estructura de Base de Datos

### Tabla: usuario

```sql
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID autoincremental',
    keycloak_sub VARCHAR(36) UNIQUE NOT NULL COMMENT 'UUID de Keycloak',
    email VARCHAR(255) NOT NULL COMMENT 'Email del usuario',
    nombre VARCHAR(255) NOT NULL COMMENT 'Nombre completo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación'
);

CREATE UNIQUE INDEX idx_keycloak_sub ON usuario(keycloak_sub);
```

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
java -jar target/gestor-personas-0.0.1-SNAPSHOT.jar
```


---

## Relación con Contribuyentes

En la arquitectura de MetaMapa:

- **Usuario** (Gestor-Personas): Representa la identidad del usuario
- **Contribuyente** (en Agregador/Dinámica): Referencia al usuario que reportó un hecho

```
Keycloak User (sub: UUID)
        ↓
Usuario (BD local, id: 1)
        ↓
Contribuyente (en Hechos, nombre: "Juan Pérez")
```
