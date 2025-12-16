# Agregador - Módulo de Gestión Central

## Descripción

El módulo **Agregador** es el servicio central de la plataforma MetaMapa que actúa como intermediario para la gestión de colecciones, hechos y solicitudes de eliminación. Proporciona una API REST completa y consultas GraphQL para acceder y manipular los datos del sistema.

## Características Principales

- 📚 **Gestión de Colecciones**: CRUD completo de colecciones de datos
- 📋 **Gestión de Hechos**: Almacenamiento y consulta de hechos/eventos
- 🔍 **Búsqueda Avanzada**: Filtrado por categoría, fechas, ubicación y búsqueda de texto libre
- 📊 **Estadísticas**: Análisis de datos agregados
- 🗑️ **Solicitudes de Eliminación**: Gestión de solicitudes de eliminación de contenido
- 🏷️ **Etiquetado**: Sistema de etiquetas para clasificar hechos
- 📍 **Geolocalización**: Soporte para ubicaciones geográficas
- 🔀 **Navegación Curada**: Navegación con curación de datos

## Tecnología

- **Framework**: Spring Boot 3.5.3
- **Lenguaje**: Java 17
- **Base de Datos**: MySQL
- **APIs**: REST + GraphQL
- **Mapeo de Objetos**: MapStruct

## Configuración

### Variables de Entorno Requeridas

```bash
# Base de datos
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/agregador
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password

# Servicios externos
ESTATICA_URL=http://localhost:8001
DINAMICA_URL=http://localhost:8002
PROXY_URL=http://localhost:8003
NORMALIZADOR_URL=http://localhost:8004
```

### Archivo de Configuración

Ver `application.properties` para más detalles de configuración:
- Puerto: 8080 (por defecto)
- Tamaño máximo de carga: 10MB
- GraphQL habilitado en `/graphql`
- Logs ubicados en `/var/log/agregador`

## Endpoints REST API

### Colecciones

#### 1. Crear Colección
```
POST /agregador/colecciones
```
**Descripción**: Crea una nueva colección en el sistema.

**Request Body**:
```json
{
  "nombre": "Mi Colección",
  "descripcion": "Descripción de la colección",
  "algoritmo_consenso": "ALGORITMO_1",
  "urls_fuente": ["http://fuente1.com", "http://fuente2.com"]
}
```

**Response**: 
- Status: 201 Created
- Body: `Integer` (ID de la colección creada)

---

#### 2. Obtener Todas las Colecciones
```
GET /agregador/colecciones
```
**Descripción**: Devuelve la lista de todas las colecciones disponibles.

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Coleccion`

---

#### 3. Obtener Colección por ID
```
GET /agregador/colecciones/{id}
```
**Descripción**: Obtiene una colección específica.

**Parámetros**:
- `id` (Path): ID de la colección

**Response**: 
- Status: 200 OK
- Body: Objeto `Coleccion`

---

#### 4. Actualizar Colección (PATCH)
```
PATCH /agregador/colecciones/{id}
```
**Descripción**: Modifica parcialmente una colección. Permite actualizar el algoritmo de consenso y/o URLs de fuentes.

**Parámetros**:
- `id` (Path): ID de la colección

**Request Body**:
```json
{
  "algoritmo_consenso": "NUEVO_ALGORITMO",
  "urls_fuente": ["http://nueva-fuente.com"]
}
```

**Response**: 
- Status: 200 OK
- Body: Objeto `Coleccion` actualizado

---

#### 5. Eliminar Colección
```
DELETE /agregador/colecciones/{id}
```
**Descripción**: Elimina una colección del sistema.

**Parámetros**:
- `id` (Path): ID de la colección

**Response**: 
- Status: 204 No Content

---

### Hechos

#### 1. Obtener Todos los Hechos
```
GET /agregador/hechos
```
**Descripción**: Obtiene todos los hechos con opciones de filtrado.

**Parámetros Query**:
- `categoria` (opcional): Filtrar por categoría
- `fecha_reporte_desde` (opcional): Fecha inicial de reporte (formato: YYYY-MM-DD)
- `fecha_reporte_hasta` (opcional): Fecha final de reporte (formato: YYYY-MM-DD)
- `fecha_acontecimiento_desde` (opcional): Fecha inicial del acontecimiento
- `fecha_acontecimiento_hasta` (opcional): Fecha final del acontecimiento
- `latitud` (opcional): Latitud para filtrado geográfico
- `longitud` (opcional): Longitud para filtrado geográfico

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `HechoDTOGraph`

**Ejemplo**:
```
GET /agregador/hechos?categoria=robo&fecha_reporte_desde=2024-01-01&latitud=-34.6037&longitud=-58.3816
```

---

#### 2. Obtener Hecho por ID
```
GET /agregador/hechos/{id}
```
**Descripción**: Obtiene un hecho específico.

**Parámetros**:
- `id` (Path): ID del hecho

**Response**: 
- Status: 200 OK
- Body: Objeto `Hecho`

---

#### 3. Obtener Hechos por Colección
```
GET /agregador/colecciones/{id}/hechos
```
**Descripción**: Obtiene todos los hechos de una colección específica con opciones de navegación y filtrado.

**Parámetros**:
- `id` (Path): ID de la colección
- `tipoNavegacion` (Query, **Requerido**): `irrestricta` o `curada`
- `categoria` (Query, opcional): Filtrar por categoría
- `fecha_reporte_desde` (Query, opcional): Fecha inicial de reporte
- `fecha_reporte_hasta` (Query, opcional): Fecha final de reporte
- `fecha_acontecimiento_desde` (Query, opcional): Fecha inicial del acontecimiento
- `fecha_acontecimiento_hasta` (Query, opcional): Fecha final del acontecimiento
- `latitud` (Query, opcional): Latitud para filtrado geográfico
- `longitud` (Query, opcional): Longitud para filtrado geográfico

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Hecho`

**Ejemplo**:
```
GET /agregador/colecciones/1/hechos?tipoNavegacion=curada&categoria=crimen
```

---

#### 4. Búsqueda de Texto Libre
```
GET /agregador/search
```
**Descripción**: Realiza una búsqueda de texto libre en los hechos.

**Parámetros Query**:
- `texto` (opcional): Término de búsqueda

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `HechoSearchDTO`

**Ejemplo**:
```
GET /agregador/search?texto=terremoto
```

---

#### 5. Obtener Ubicaciones
```
GET /agregador/hechos/ubicaciones
```
**Descripción**: Obtiene todas las ubicaciones de los hechos para visualización en mapas.

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `UbicacionParaMapaDTO`

---

### Categorías

#### 1. Obtener Todas las Categorías
```
GET /agregador/categorias
```
**Descripción**: Obtiene la lista de todas las categorías disponibles.

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `CategoriaDTO`

---

### Solicitudes de Eliminación

#### 1. Obtener Todas las Solicitudes
```
GET /agregador/solicitudes
```
**Descripción**: Obtiene todas las solicitudes de eliminación.

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `SolicitudDTOE`

---

#### 2. Obtener Solicitudes Pendientes
```
GET /agregador/solicitudes/pendientes
```
**Descripción**: Obtiene solo las solicitudes que están en estado pendiente.

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `SolicitudDTOE`

---

#### 3. Crear Solicitud de Eliminación
```
POST /agregador/solicitudes
```
**Descripción**: Crea una nueva solicitud de eliminación de un hecho.

**Request Body**:
```json
{
  "hecho_id": 123,
  "razon": "Contenido inapropiado",
  "estado": "PENDIENTE"
}
```

**Response**: 
- Status: 201 Created
- Body: `Integer` (ID de la solicitud creada)

---

#### 4. Actualizar Estado de Solicitud
```
PUT /agregador/solicitudes/{id}
```
**Descripción**: Modifica el estado de una solicitud (aceptar o rechazar).

**Parámetros**:
- `id` (Path): ID de la solicitud

**Request Body** (Enum `Estado_Solicitud`):
```json
"ACEPTADA"  // o "RECHAZADA", "PENDIENTE"
```

**Response**: 
- Status: 200 OK
- Body: Objeto `SolicitudDTOE` actualizado

---

## API GraphQL

### Ubicación
```
POST /graphql
```

### Esquema de Queries Disponibles

#### 1. Obtener Todos los Hechos
```graphql
query {
  hechos {
    id
    titulo
    descripcion
    categoria { nombre }
    ubicacion { latitud longitud }
    fechaAcontecimiento
    fechaCarga
    etiquetas { nombre }
    contribuyente { id nombre }
  }
}
```

---

#### 2. Obtener Hecho por ID
```graphql
query {
  hecho(id: 1) {
    id
    titulo
    descripcion
    cuerpo  # para HechoTextual
    # urls  # para HechoMultimedia
  }
}
```

---

#### 3. Obtener Hechos por Contribuyente
```graphql
query {
  hechosPorContribuyente(contribuyenteId: 5) {
    id
    titulo
    contribuyente { nombre }
  }
}
```

---

#### 4. Obtener Todas las Colecciones
```graphql
query {
  colecciones {
    id
    nombre
    descripcion
    algoritmoConsenso
    urlsFuente
  }
}
```

---

#### 5. Obtener Colección por ID
```graphql
query {
  coleccion(id: 1) {
    id
    nombre
    descripcion
  }
}
```

---

#### 6. Obtener Todas las Solicitudes
```graphql
query {
  solicitudes {
    id
    hechoId
    razon
    estado
    fecha
  }
}
```

---

#### 7. Obtener Solicitudes Pendientes
```graphql
query {
  solicitudesPendientes {
    id
    razon
    estado
  }
}
```

---

#### 8. Obtener Solicitud por ID
```graphql
query {
  solicitud(id: 1) {
    id
    razon
    estado
    fecha
  }
}
```

---

#### 9. Obtener Contribuyente
```graphql
query {
  contribuyente(id: 1) {
    id
    nombre
  }
}
```

---

#### 10. Obtener Hechos por Etiquetas
```graphql
query {
  hechosPorEtiquetas(nombres: ["violencia", "crimen"], match: "ALL") {
    id
    titulo
    etiquetas { nombre }
  }
}
```

**Parámetro `match`**: 
- `"ANY"` (por defecto): Hechos que tengan al menos una de las etiquetas
- `"ALL"`: Hechos que tengan todas las etiquetas especificadas

---

## Estadísticas

### 1. Obtener Ubicaciones de una Colección
```
GET /agregador/estadisticas/coleccion/{id}/ubicaciones
```

**Parámetros**:
- `id` (Path): ID de la colección

**Response**: Lista de `UbicacionDTO`

---

### 2. Obtener Categorías con Más Hechos
```
GET /agregador/estadisticas/hechos/max-categoria/{cantidadCategorias}
```

**Parámetros**:
- `cantidadCategorias` (Path): Cantidad de categorías a retornar

**Response**: Lista de `Categoria`

---

### 3. Obtener Ubicaciones de una Categoría
```
GET /agregador/estadisticas/categoria/{id}/ubicaciones
```

**Parámetros**:
- `id` (Path): ID de la categoría

**Response**: Lista de `UbicacionDTO`

---

### 4. Obtener Horas Más Frecuentes de una Categoría
```
GET /agregador/estadisticas/categoria/{id}/hora/{cantidadHoras}
```

**Parámetros**:
- `id` (Path): ID de la categoría
- `cantidadHoras` (Path): Cantidad de horas a retornar

**Response**: Lista de `LocalTime`

---

### 5. Obtener Solicitudes Spam
```
GET /agregador/estadisticas/solicitudes/spam
```

**Response**: Lista de `SolicitudDTOE` identificadas como spam

---

## Modelos de Datos

### Hecho
```json
{
  "id": 1,
  "titulo": "Título del hecho",
  "descripcion": "Descripción detallada",
  "categoria": {
    "id": 1,
    "nombre": "Robo"
  },
  "ubicacion": {
    "id": 1,
    "latitud": -34.6037,
    "longitud": -58.3816,
    "descripcion": "Buenos Aires"
  },
  "fechaAcontecimiento": "2024-12-15T10:30:00",
  "fechaCarga": "2024-12-15T14:20:00",
  "origenFuente": {
    "id": 1,
    "nombre": "Twitter",
    "url": "https://twitter.com/..."
  },
  "etiquetas": [
    { "id": 1, "nombre": "violencia" },
    { "id": 2, "nombre": "seguridad" }
  ],
  "contribuyente": {
    "id": 5,
    "nombre": "Usuario X"
  }
}
```

### Colección
```json
{
  "id": 1,
  "nombre": "Mi Colección",
  "descripcion": "Descripción de la colección",
  "algoritmoConsenso": "ALGORITMO_1",
  "urlsFuente": [
    "http://fuente1.com",
    "http://fuente2.com"
  ]
}
```

### Solicitud de Eliminación
```json
{
  "id": 1,
  "hechoId": 123,
  "razon": "Contenido inapropiado",
  "estado": "PENDIENTE",
  "fecha": "2024-12-15T14:20:00"
}
```

---

## Ejemplos de Uso

### Ejemplo 1: Crear una colección y obtener sus hechos
```bash
# 1. Crear colección
curl -X POST http://localhost:8080/agregador/colecciones \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Delitos de Buenos Aires",
    "descripcion": "Colección de delitos reportados en CABA",
    "algoritmo_consenso": "MAYORIA",
    "urls_fuente": ["http://datos.buenosaires.gob.ar"]
  }'

# Respuesta: 1 (ID de la colección)

# 2. Obtener hechos de la colección
curl http://localhost:8080/agregador/colecciones/1/hechos?tipoNavegacion=curada
```

### Ejemplo 2: Búsqueda filtrada de hechos
```bash
curl "http://localhost:8080/agregador/hechos?categoria=robo&fecha_reporte_desde=2024-01-01&fecha_reporte_hasta=2024-12-31&latitud=-34.6037&longitud=-58.3816"
```

### Ejemplo 3: Consulta GraphQL para hechos
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { hechos { id titulo categoria { nombre } } }"
  }'
```

### Ejemplo 4: Gestión de solicitudes
```bash
# Crear solicitud de eliminación
curl -X POST http://localhost:8080/agregador/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "hecho_id": 123,
    "razon": "Información falsa",
    "estado": "PENDIENTE"
  }'

# Obtener solicitudes pendientes
curl http://localhost:8080/agregador/solicitudes/pendientes

# Aceptar una solicitud
curl -X PUT http://localhost:8080/agregador/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '"ACEPTADA"'
```

---

## Monitoreo y Logs

### Métricas Disponibles
- Health check: `/actuator/health`
- Métricas de Prometheus: `/actuator/prometheus`
- Información: `/actuator/info`

### Logs
Los logs se almacenan en `/var/log/agregador/agregador.log`

---

## Manejo de Errores

El servicio devuelve códigos HTTP estándar:

- `200 OK`: Solicitud exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Recurso eliminado exitosamente
- `400 Bad Request`: Solicitud inválida
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error en el servidor

### Ejemplo de Error
```json
{
  "timestamp": "2024-12-15T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Colección no encontrada",
  "path": "/agregador/colecciones/999"
}
```

---

## Integración con Otros Módulos

El Agregador se integra con:

- **Estática** (`estatica.url`): Servicios de datos estáticos
- **Dinámica** (`dinamica.url`): Servicios de datos dinámicos
- **Proxy** (`proxy.url`): Proxy inverso para cacheo
- **Normalizador** (`normalizador.url`): Normalización de datos

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

