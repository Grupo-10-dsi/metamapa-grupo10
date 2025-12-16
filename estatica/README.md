# Estática - Módulo de Importación de Datos Estáticos

## Descripción

El módulo **Estática** es responsable de importar y procesar datos estáticos de fuentes de información CSV. Proporciona una API para:

- 📥 **Importar datos de archivos CSV** precargados en el sistema
- 🗂️ **Procesar múltiples fuentes** simultáneamente
- 📊 **Transformar datos CSV en hechos** estructurados
- 🔍 **Acceder a datos históricos** de eventos y desastres
- 📈 **Agregar datos estáticos al sistema** junto con datos dinámicos

## Características Principales

- 📁 **Importación de CSV**: Soporte para múltiples archivos CSV predefinidos
- 🔄 **Procesamiento Dinámico**: Importa datos bajo demanda
- 📍 **Geolocalización**: Hechos con ubicación geográfica
- 📅 **Clasificación Temporal**: Hechos con fechas de acontecimiento
- 🏷️ **Categorización**: Hechos agrupados por categoría
- 📦 **Formato Estandarizado**: Respuestas en formato JSON estructurado
- 🔐 **Origen Identificado**: Todos los hechos marcan su origen como "ESTATICA"

## Tecnología

- **Framework**: Spring Boot 3.5.0
- **Lenguaje**: Java 17
- **Procesamiento de CSV**: OpenCSV 5.7.1
- **API**: REST
- **Monitoreo**: Micrometer + Prometheus
- **Almacenamiento**: En memoria (no persiste en BD)

## Configuración

### Variables de Entorno

No requiere variables de entorno adicionales. La configuración es mínima:

```bash
# Puerto (por defecto)
SERVER_PORT=8081

# Logs
LOGGING_FILE_PATH=/var/log/estatica
```

### Configuración Principal

- **Puerto**: 8081
- **Ruta Base**: `/api/estatica`
- **Logs**: `/var/log/estatica/estatica.log`
- **Almacenamiento**: En memoria (se reinicia con cada solicitud)

---

## Archivos CSV Disponibles

### 1. **desastres.csv**
Contiene información sobre desastres en Buenos Aires.

**Campos CSV**:
- `Título`: Nombre del evento
- `Descripción`: Descripción detallada
- `Categoría`: Tipo de desastre (ej: "inundacion")
- `Latitud`: Coordenada de latitud
- `Longitud`: Coordenada de longitud
- `Fecha del hecho`: Fecha del evento (formato DD/MM/YYYY)

**Contenido de ejemplo**:
```
"Inundaciones en Buenos Aires dejan a miles sin hogar",
"Las fuertes lluvias en Buenos Aires...",
"inundacion",
-34.603722,
-58.381592,
"10/04/2013"
```

---

### 2. **terremotos.csv**
Contiene información sobre incendios y terremotos.

**Campos**: Mismos que desastres.csv

**Contenido de ejemplo**:
```
"Incendio en depósito de Villa Lugano provoca gran columna de humo",
"Un incendio de gran magnitud afectó...",
"Incendios",
-34.6784,
-58.4743,
"12/03/2018"
```

---

### 3. **desastres_naturales_argentina.csv**
Contiene información sobre desastres naturales en Argentina.

**Campos**: Mismos que desastres.csv

**Contenido de ejemplo**:
```
"Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones",
"La región de San Vicente...",
"Ráfagas de más de 100 km/h",
-27.029465,
-54.436559,
"21/12/2007"
```

---

## Endpoints REST API

### Obtener Hechos de Archivos Estáticos

```
GET /api/estatica/hechos
```

**Descripción**: Importa hechos de uno o más archivos CSV especificados. Los datos se procesan bajo demanda y se devuelven en formato JSON.

**Parámetros Query**:
- `archivosProcesados` (Requerido): Lista de nombres de archivos a procesar (sin la extensión .csv)

**Archivos Válidos**:
- `desastres`
- `terremotos`
- `desastres_naturales_argentina`

**Response**: 
- Status: 200 OK
- Body: Lista de `ArchivoProcesadoDTO` (ver modelos de datos)

**Ejemplos**:

#### Ejemplo 1: Importar un archivo
```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres"
```

#### Ejemplo 2: Importar múltiples archivos
```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres&archivosProcesados=terremotos"
```

#### Ejemplo 3: Importar todos los archivos
```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres&archivosProcesados=terremotos&archivosProcesados=desastres_naturales_argentina"
```

#### Ejemplo 4: Con jq para mejor legibilidad
```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres" | jq '.'
```

---

## Modelos de Datos

### ArchivoProcesadoDTO (Response)

```json
{
  "nombre": "desastres",
  "fechaCarga": "2024-12-15T15:30:45.123456",
  "hechos": [
    {
      "id": 1,
      "titulo": "Inundaciones en Buenos Aires dejan a miles sin hogar",
      "descripcion": "Las fuertes lluvias en Buenos Aires...",
      "categoria": {
        "id": null,
        "detalle": "inundacion"
      },
      "ubicacion": {
        "id": null,
        "latitud": -34.603722,
        "longitud": -58.381592,
        "descripcion": null
      },
      "fechaAcontecimiento": "2013-04-10T00:00:00",
      "fechaCarga": "2024-12-15T15:30:45.123456",
      "contenidoMultimedia": null,
      "cuerpo": null
    }
  ]
}
```

---

### HechoDTO (Dentro de ArchivoProcesadoDTO)

```json
{
  "id": 1,
  "titulo": "string",
  "descripcion": "string",
  "categoria": {
    "id": null,
    "detalle": "string"
  },
  "ubicacion": {
    "id": null,
    "latitud": -34.603722,
    "longitud": -58.381592,
    "descripcion": null
  },
  "fechaAcontecimiento": "2024-12-15T10:00:00",
  "fechaCarga": "2024-12-15T15:30:45.123456",
  "contenidoMultimedia": null,
  "cuerpo": null
}
```

**Campos**:
- `id`: Identificador único del hecho
- `titulo`: Título del evento
- `descripcion`: Descripción detallada
- `categoria`: Objeto con categoría del evento
- `ubicacion`: Objeto con coordenadas geográficas
- `fechaAcontecimiento`: Fecha y hora del evento (ISO 8601)
- `fechaCarga`: Fecha y hora de importación al sistema
- `contenidoMultimedia`: URLs de archivos multimedia (null para estática)
- `cuerpo`: Contenido textual adicional (null para estática)

---

### Hecho (Entidad Interna)

```java
{
  id: Integer,
  titulo: String,
  descripcion: String,
  categoria: Categoria,
  ubicacion: Ubicacion,
  fechaAcontecimiento: LocalDateTime,
  fechaCarga: LocalDateTime,
  origenFuente: Origen_Fuente.ESTATICA
}
```

---

## Particularidades de la Implementación

### 1. **Importación Bajo Demanda**
Los datos se importan solo cuando se solicitan. Cada llamada al endpoint importa frescos los datos de los CSV.

### 2. **Almacenamiento en Memoria**
- Los datos se mantienen en una lista en memoria dentro de `HechosRepository`
- No se persisten en base de datos
- Se limpian con cada nueva importación

### 3. **Origen Identificado**
Todos los hechos importados tienen:
```java
origenFuente = Origen_Fuente.ESTATICA
```

### 4. **Fecha de Carga Automática**
Se asigna automáticamente la fecha/hora actual al campo `fechaCarga`:
```java
this.fechaCarga = LocalDateTime.now();
```

### 5. **Formato de Fecha CSV**
Los archivos CSV contienen fechas en formato **DD/MM/YYYY**:
```
10/04/2013
```
Se convierten automáticamente a **ISO 8601** en la respuesta:
```json
"fechaAcontecimiento": "2013-04-10T00:00:00"
```

---

## Integración con el Sistema

### Consumo por Otros Módulos

El módulo Estática es consumido por:
- **Agregador**: Importa hechos estáticos para enriquecer búsquedas
- **API Gateway**: Enruta solicitudes a este módulo
- **Visualizador**: Muestra datos estáticos en mapas y listados


## Ejemplos de Uso Completo

### Ejemplo 1: Obtener desastres de Buenos Aires

```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres" | jq '.'
```

**Response**:
```json
[
  {
    "nombre": "desastres",
    "fechaCarga": "2024-12-15T15:35:20.456789",
    "hechos": [
      {
        "id": 1,
        "titulo": "Inundaciones en Buenos Aires dejan a miles sin hogar",
        "descripcion": "Las fuertes lluvias en Buenos Aires...",
        "categoria": {
          "detalle": "inundacion"
        },
        "ubicacion": {
          "latitud": -34.603722,
          "longitud": -58.381592
        },
        "fechaAcontecimiento": "2013-04-10T00:00:00",
        "fechaCarga": "2024-12-15T15:35:20.456789"
      }
    ]
  }
]
```

---

### Ejemplo 2: Obtener múltiples fuentes

```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres&archivosProcesados=terremotos" | jq '.[] | {nombre: .nombre, cantidad: (.hechos | length)}'
```

**Output**:
```json
{
  "nombre": "desastres",
  "cantidad": 1
}
{
  "nombre": "terremotos",
  "cantidad": 2
}
```

---

### Ejemplo 3: Combinar con Dinámica (En aplicación cliente)

```javascript
// Obtener datos estáticos
const estaticaResponse = await fetch('http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres');
const estaticaData = await estaticaResponse.json();

// Obtener datos dinámicos
const dinamicaResponse = await fetch('http://localhost:8089/api/dinamica/hechos');
const dinamicaData = await dinamicaResponse.json();

// Combinar
const todosLosHechos = [
  ...estaticaData.flatMap(a => a.hechos),
  ...dinamicaData
];

// Mostrar en mapa
mostrarEnMapa(todosLosHechos);
```

---

### Ejemplo 4: Filtrar por categoría

```bash
# Obtener todos los incendios (de terremotos.csv)
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=terremotos" | \
  jq '.[] | .hechos[] | select(.categoria.detalle == "Incendios")'
```

---

### Ejemplo 5: Extraer ubicaciones para mapa

```bash
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres_naturales_argentina" | \
  jq '.[] | .hechos[] | {titulo: .titulo, lat: .ubicacion.latitud, lng: .ubicacion.longitud}'
```

**Output**:
```json
{
  "titulo": "Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones",
  "lat": -27.029465,
  "lng": -54.436559
}
{
  "titulo": "Incendios forestales en Córdoba arrasan con miles de hectáreas",
  "lat": -31.420083,
  "lng": -64.188776
}
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
  "status": "UP"
}
```

---

### Métricas de Prometheus
```
GET /actuator/prometheus
```

Devuelve métricas incluyendo:
- Latencia de importación
- Número de hechos importados
- Errores en parsing de CSV
- Información de la JVM

---

### Logs

Ubicación: `/var/log/estatica/estatica.log`

**Ejemplos de logs**:
```
INFO  - Listando hechos para los archivos: [desastres, terremotos]
INFO  - Se importaron 3 hechos de 2 archivos procesados.
DEBUG - Importando hechos...
```

---

## Manejo de Errores

### Error 400: Bad Request
**Causa**: Archivo inválido o no especificado
**Solución**: Verificar nombres de archivos válidos

```bash
# ❌ Incorrecto
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=archivo_inexistente"

# ✅ Correcto
curl "http://localhost:8089/api/estatica/hechos?archivosProcesados=desastres"
```

### Error 500: Internal Server Error
**Causa**: Problema en parsing de CSV o formato incorrecto
**Solución**: Verificar integridad de archivos CSV



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
java -jar target/estatica-0.0.1-SNAPSHOT.jar
```



