# Proxy - Módulo de Agregación y Enrutamiento de Datos

## Descripción

El módulo **Proxy** actúa como intermediario para acceder a hechos desde múltiples fuentes:

- 📡 **Conexiones Demo**: Acceso a fuentes estáticas de prueba
- 🔗 **Instancias MetaMapa**: Conexión a múltiples instancias de MetaMapa
- 🔄 **Sincronización**: Actualización periódica de datos (cada 1 hora)
- 🏠 **Caché Local**: Almacenamiento en memoria de hechos
- 🔍 **Filtrado Avanzado**: Filtros por categoría, fechas y ubicación
- 🌐 **Cliente HTTP Reactivo**: WebClient para conexiones asincrónicas
- 📊 **Agregación de Fuentes**: Combina datos de múltiples instancias

## Características Principales

- 🔀 **Enrutamiento de Solicitudes**: Dirige consultas a la fuente correcta
- 📥 **Sincronización Automática**: Actualiza hechos cada 1 hora
- 🗺️ **Filtrado Completo**: Por categoría, fechas, ubicación y antigüedad
- ⚡ **Reactividad**: WebFlux para operaciones no bloqueantes
- 🔗 **Múltiples Instancias MetaMapa**: Soporte para múltiples servidores
- 💾 **Caché Local**: Almacenamiento de hechos en repositorio local
- 📝 **Mapeo de Tipos**: Conversión automática entre HechoDto y entidades

## Tecnología

- **Framework**: Spring Boot 3.5.0
- **Lenguaje**: Java 17
- **Cliente HTTP**: WebClient (WebFlux)
- **Almacenamiento**: Repositorio en memoria
- **API**: REST
- **Tareas Programadas**: @Scheduled (cada 1 hora)
- **Monitoreo**: Micrometer + Prometheus

## Configuración

### Variables de Entorno Requeridas

```bash
# Instancia MetaMapa a conectar
METAMAPA_INSTANCIA_URL=http://localhost:8089
```

### Configuración Principal

- **Puerto**: 8083
- **Ruta Base**: `/api/proxy`
- **Frecuencia de Sincronización**: 1 hora (3,600,000 ms)
- **Logs**: `/var/log/proxy/`

---

## Endpoints REST API

### 1. Obtener Todos los Hechos

```
GET /api/proxy/hechos
```

**Descripción**: Obtiene todos los hechos disponibles (locales + MetaMapa) filtrados por antigüedad.

**Parámetros Query**:
- `ultimaConsulta` (opcional): ISO DateTime - Devuelve solo hechos posteriores a esta fecha

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Hecho`

**Ejemplo**:
```bash
# Obtener todos los hechos
curl http://localhost:8083/api/proxy/hechos

# Obtener hechos desde una fecha específica
curl "http://localhost:8083/api/proxy/hechos?ultimaConsulta=2024-12-15T10:00:00"
```

---

### 2. Obtener Hechos de Conexión Demo

```
GET /api/proxy/demo/hechos/{nombreConexion}
```

**Descripción**: Obtiene hechos de una fuente de prueba local configurada.

**Parámetros**:
- `nombreConexion` (Path, obligatorio): Nombre de la conexión demo (ej: "desastres", "terremotos")

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Hecho`
- Status: 404 Not Found - Si no existe la conexión

**Ejemplo**:
```bash
curl http://localhost:8083/api/proxy/demo/hechos/desastres
```

---

### 3. Obtener Todos los Hechos de MetaMapa

```
GET /api/proxy/metaMapa/hechos
```

**Descripción**: Obtiene todos los hechos de instancias MetaMapa configuradas con filtrado avanzado.

**Parámetros Query** (todos opcionales):
- `categoria`: Filtrar por categoría
- `fecha_reporte_desde`: Fecha inicial de carga (ISO DateTime)
- `fecha_reporte_hasta`: Fecha final de carga (ISO DateTime)
- `fecha_acontecimiento_desde`: Fecha inicial del evento (ISO DateTime)
- `fecha_acontecimiento_hasta`: Fecha final del evento (ISO DateTime)
- `latitud`: Coordenada de latitud
- `longitud`: Coordenada de longitud
- `ultimaConsulta`: ISO DateTime - Hechos posteriores a esta fecha

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Hecho`

**Ejemplos**:
```bash
# Obtener todos los hechos de MetaMapa
curl http://localhost:8083/api/proxy/metaMapa/hechos

# Filtrar por categoría
curl "http://localhost:8083/api/proxy/metaMapa/hechos?categoria=robo"

# Filtrar por rango de fechas
curl "http://localhost:8083/api/proxy/metaMapa/hechos?fecha_reporte_desde=2024-12-01T00:00:00&fecha_reporte_hasta=2024-12-31T23:59:59"

# Filtrar por ubicación
curl "http://localhost:8083/api/proxy/metaMapa/hechos?latitud=-34.6037&longitud=-58.3816"

# Filtro combinado
curl "http://localhost:8083/api/proxy/metaMapa/hechos?categoria=crimen&latitud=-34.6037&longitud=-58.3816&fecha_reporte_desde=2024-12-01T00:00:00"
```

---

### 4. Obtener Hechos de Colección Específica de MetaMapa

```
GET /api/proxy/metaMapa/colecciones/{identificador}/hechos
```

**Descripción**: Obtiene hechos de una colección específica en MetaMapa con filtrado.

**Parámetros**:
- `identificador` (Path, obligatorio): ID o handle de la colección
- `categoria` (Query, opcional): Filtrar por categoría
- `fecha_reporte_desde` (Query, opcional): Fecha inicial de carga
- `fecha_reporte_hasta` (Query, opcional): Fecha final de carga
- `fecha_acontecimiento_desde` (Query, opcional): Fecha inicial del evento
- `fecha_acontecimiento_hasta` (Query, opcional): Fecha final del evento
- `latitud` (Query, opcional): Coordenada de latitud
- `longitud` (Query, opcional): Coordenada de longitud
- `ultimaConsulta` (Query, opcional): Hechos posteriores a esta fecha

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Hecho`

**Ejemplos**:
```bash
# Obtener todos los hechos de una colección
curl http://localhost:8083/api/proxy/metaMapa/colecciones/1/hechos

# Con filtros
curl "http://localhost:8083/api/proxy/metaMapa/colecciones/1/hechos?categoria=robo&fecha_reporte_desde=2024-12-01T00:00:00"

# Filtrar por ubicación
curl "http://localhost:8083/api/proxy/metaMapa/colecciones/1/hechos?latitud=-34.6037&longitud=-58.3816"
```

---

### 5. Crear Solicitud de Eliminación en MetaMapa

```
POST /api/proxy/metaMapa/solicitudes
```

**Descripción**: Crea una solicitud de eliminación para un hecho en MetaMapa.

**Request Body** (SolicitudEliminacionDTO):
```json
{
  "idHecho": "123e4567-e89b-12d3-a456-426614174000",
  "justificacion": "Contenido inapropiado o inexacto"
}
```

**Parámetros**:
- `idHecho` (UUID, obligatorio): ID del hecho a eliminar
- `justificacion` (String, obligatorio): Razón de la solicitud

**Response**: 
- Status: 200 OK / 201 Created
- Body: `SolicitudEliminacion` creada
- Status: 404 Not Found - Si el hecho no existe

**Ejemplo**:
```bash
curl -X POST http://localhost:8083/api/proxy/metaMapa/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "idHecho": "550e8400-e29b-41d4-a716-446655440000",
    "justificacion": "Información incorrecta o spam"
  }'
```

---

## Modelos de Datos

### HechoDto (Request/Response)

DTO para hechos desde MetaMapa:

```json
{
  "titulo": "Robo en San Isidro",
  "descripcion": "Se reportó un robo a mano armada",
  "categoria": {
    "detalle": "Robo"
  },
  "ubicacion": {
    "latitud": -34.4835,
    "longitud": -58.5249,
    "descripcion": "San Isidro, Buenos Aires"
  },
  "fechaAcontecimiento": "2024-12-15T14:30:00",
  "fechaCarga": "2024-12-15T15:00:00",
  "origenFuente": "METAMAPA",
  "etiquetas": [
    { "nombre": "violencia" }
  ],
  "contribuyente": {
    "id": 1,
    "nombre": "Usuario X"
  },
  "contenidoMultimedia": ["url1.jpg"],
  "cuerpo": "Descripción textual del evento"
}
```

### Hecho (Entidad)

Entidad que representa un hecho en el sistema:

```json
{
  "id": 1,
  "titulo": "Robo en San Isidro",
  "descripcion": "Se reportó un robo",
  "categoria": { "detalle": "Robo" },
  "ubicacion": {
    "latitud": -34.4835,
    "longitud": -58.5249
  },
  "fechaAcontecimiento": "2024-12-15T14:30:00",
  "fechaCarga": "2024-12-15T15:00:00",
  "origenFuente": "METAMAPA",
  "etiquetas": [{ "nombre": "urgente" }],
  "contribuyente": { "nombre": "Usuario X" },
  "contenidoMultimedia": [],
  "cuerpo": "Contenido"
}
```

### SolicitudEliminacionDTO (Request)

```json
{
  "idHecho": "550e8400-e29b-41d4-a716-446655440000",
  "justificacion": "Contenido inapropiado"
}
```

### SolicitudEliminacion (Response)

```json
{
  "id": 1,
  "idHecho": "550e8400-e29b-41d4-a716-446655440000",
  "justificacion": "Contenido inapropiado",
  "estado": "PENDIENTE",
  "fechaCreacion": "2024-12-15T15:00:00"
}
```

---


## Ejemplos de Uso Completo

### Ejemplo 1: Obtener hechos locales + MetaMapa

```bash
curl http://localhost:8083/api/proxy/hechos | jq '.[] | {titulo: .titulo, origen: .origenFuente}'
```

**Response**:
```json
{
  "titulo": "Incendio forestal en Córdoba",
  "origen": "ESTATICA"
}
{
  "titulo": "Robo en San Isidro",
  "origen": "METAMAPA"
}
```

---

### Ejemplo 2: Obtener hechos con filtro de antigüedad

```bash
curl "http://localhost:8083/api/proxy/hechos?ultimaConsulta=2024-12-14T00:00:00"
```

Retorna solo hechos posteriores a 2024-12-14.

---

### Ejemplo 3: Filtrado completo de MetaMapa

```bash
curl "http://localhost:8083/api/proxy/metaMapa/hechos?categoria=robo&fecha_reporte_desde=2024-12-01T00:00:00&latitud=-34.6037&longitud=-58.3816"
```

Combina tres filtros en una sola solicitud.

---

### Ejemplo 4: Hechos de colección específica

```bash
curl "http://localhost:8083/api/proxy/metaMapa/colecciones/1/hechos?categoria=crimen"
```

---

### Ejemplo 5: Crear solicitud de eliminación

```bash
curl -X POST http://localhost:8083/api/proxy/metaMapa/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "idHecho": "550e8400-e29b-41d4-a716-446655440000",
    "justificacion": "Contenido verificado como falso"
  }' | jq '.'
```

---

## Monitoreo y Logs

### Health Check
```
GET /actuator/health
```

### Métricas de Prometheus
```
GET /actuator/prometheus
```

### Logs
```
GET /api/proxy/health → "Servicio de proxy activo"
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
java -jar target/proxy-0.0.1-SNAPSHOT.jar
```

---

## Configuración para Múltiples Instancias MetaMapa

### Actual (Una sola instancia)
```java
@PostConstruct
public void inicializarInstancias() {
    this.instanciasMetaMapa.add(new MetaMapaClient(metaMapaUrl));
}
```

### Mejora Futura (Múltiples instancias desde config)
```properties
# application.properties
metamapa.instancias[0].url=http://localhost:8089
metamapa.instancias[1].url=http://otro-servidor:8089
metamapa.instancias[2].url=http://tercer-servidor:8089
```


## API Summary

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/proxy/hechos` | Todos los hechos (local + MetaMapa) |
| GET | `/api/proxy/demo/hechos/{nombre}` | Hechos de conexión demo |
| GET | `/api/proxy/metaMapa/hechos` | Todos de MetaMapa con filtros |
| GET | `/api/proxy/metaMapa/colecciones/{id}/hechos` | Hechos de colección con filtros |
| POST | `/api/proxy/metaMapa/solicitudes` | Crear solicitud de eliminación |

