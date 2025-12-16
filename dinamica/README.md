# Dinámica - Módulo de Gestión de Contenido Dinámico

## Descripción

El módulo **Dinámica** es responsable de la gestión dinámica de contenido en la plataforma MetaMapa. Proporciona endpoints para:

- 📝 **Crear y editar hechos** (eventos/noticias) de tipo textual y multimedia
- 📸 **Cargar contenido multimedia** (imágenes) a Supabase
- 🔍 **Filtrar y buscar hechos** con múltiples criterios
- 🗑️ **Gestionar solicitudes de eliminación** con detección automática de spam
- 📊 **Validación de contenido** antes de ser almacenado

## Características Principales

- 📝 **Gestión Flexible de Hechos**: Soporte para hechos textuales y multimedia
- 🖼️ **Almacenamiento de Imágenes**: Integración con Supabase para contenido multimedia
- 🔍 **Filtrado Avanzado**: Por categoría, fechas, ubicación y hora de última consulta
- 🚫 **Detección de Spam**: Análisis automático usando corpus de palabras clave
- ✅ **Validación de Datos**: Verificación de campos obligatorios
- 📱 **API REST Completa**: Operaciones CRUD en hechos y solicitudes
- 📊 **Métricas y Monitoreo**: Health checks y métricas de Prometheus

## Tecnología

- **Framework**: Spring Boot 3.5.0
- **Lenguaje**: Java 17
- **Base de Datos**: MySQL
- **Almacenamiento de Archivos**: Supabase (S3-compatible)
- **API**: REST
- **ORM**: Spring Data JPA (Hibernate)
- **Monitoreo**: Micrometer + Prometheus

## Configuración

### Variables de Entorno Requeridas

```bash
# Base de datos
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/dinamica
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password

# Supabase (Almacenamiento de archivos)
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_BUCKET=metamapa-bucket
SUPABASE_SERVICE_KEY=your-service-key

# Servicios externos
GESTOR_PERSONAS_URL=http://localhost:8001
```

### Configuración Principal

- **Puerto**: 8082
- **DDL Strategy**: update (actualiza esquema automáticamente)
- **Logs**: `/var/log/dinamica/dinamica.log`
- **GraphQL**: No habilitado en este módulo (disponible en Agregador)

---

## Endpoints REST API

### Hechos

#### 1. Obtener Hechos Filtrados
```
GET /api/dinamica/hechos
```
**Descripción**: Obtiene hechos con filtrado avanzado. Todos los parámetros son opcionales.

**Parámetros Query**:
- `ultimaConsulta` (opcional): ISO DateTime - Devuelve hechos creados después de esta fecha
- `categoria` (opcional): Nombre de la categoría (ej: "robo", "crimen")
- `fecha_reporte_desde` (opcional): ISO DateTime - Fecha inicial de carga del hecho
- `fecha_reporte_hasta` (opcional): ISO DateTime - Fecha final de carga del hecho
- `fecha_acontecimiento_desde` (opcional): ISO DateTime - Fecha inicial del evento
- `fecha_acontecimiento_hasta` (opcional): ISO DateTime - Fecha final del evento
- `latitud` (opcional): Coordenada de latitud para filtrado geográfico
- `longitud` (opcional): Coordenada de longitud para filtrado geográfico

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `Hecho`

**Ejemplos**:
```bash
# Obtener todos los hechos
GET /api/dinamica/hechos

# Filtrar por categoría
GET /api/dinamica/hechos?categoria=robo

# Filtrar por rango de fechas
GET /api/dinamica/hechos?fecha_reporte_desde=2024-01-01T00:00:00&fecha_reporte_hasta=2024-12-31T23:59:59

# Filtrar por ubicación
GET /api/dinamica/hechos?latitud=-34.6037&longitud=-58.3816

# Filtrar por última consulta (para sincronización)
GET /api/dinamica/hechos?ultimaConsulta=2024-12-14T10:30:00

# Filtro combinado
GET /api/dinamica/hechos?categoria=crimen&latitud=-34.6037&longitud=-58.3816&fecha_reporte_desde=2024-12-01T00:00:00
```

---

#### 2. Crear Hecho
```
POST /api/dinamica/hechos
```
**Descripción**: Crea un nuevo hecho (textual o multimedia) en el sistema. El hecho se crea automáticamente asociado a un contribuyente (anónimo si no se proporciona).

**Request Body** (HechoDTO):
```json
{
  "tipo": "textual",
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
  "etiquetas": [
    {
      "nombre": "violencia"
    },
    {
      "nombre": "seguridad"
    }
  ],
  "contribuyente": null,
  "cuerpo": "En las últimas horas se reportó un robo a mano armada..."
}
```

**Para hechos multimedia**:
```json
{
  "tipo": "multimedia",
  "titulo": "Video de accidente vehicular",
  "descripcion": "Registro de video del accidente",
  "categoria": {
    "detalle": "Accidente"
  },
  "ubicacion": {
    "latitud": -34.6037,
    "longitud": -58.3816,
    "descripcion": "Buenos Aires"
  },
  "fechaAcontecimiento": "2024-12-15T10:00:00",
  "etiquetas": [
    {
      "nombre": "tráfico"
    }
  ],
  "contribuyente": null,
  "contenidoMultimedia": ["video1.mp4", "imagen1.jpg"]
}
```

**Campos Obligatorios**:
- `tipo`: "textual" o "multimedia"
- `titulo`: Título del hecho
- `descripcion`: Descripción general
- `categoria.detalle`: Categoría a la que pertenece
- `ubicacion`: Objeto con latitud, longitud y descripción
- `fechaAcontecimiento`: Fecha en que ocurrió el evento (ISO DateTime)
- `etiquetas`: Lista de etiquetas para clasificación

**Campos Condicionales**:
- `cuerpo`: **Obligatorio para tipo "textual"** - Contenido textual detallado
- `contenidoMultimedia`: **Obligatorio para tipo "multimedia"** - Lista de URLs o nombres de archivos

**Campos Opcionales**:
- `contribuyente`: Información del usuario que reporta (null = anónimo)

**Response**: 
- Status: 201 Created
- Body: `Integer` (ID del hecho creado)

**Ejemplo**:
```bash
curl -X POST http://localhost:8089/api/dinamica/hechos \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "textual",
    "titulo": "Robo confirmado",
    "descripcion": "Evento reportado",
    "categoria": { "detalle": "Robo" },
    "ubicacion": {
      "latitud": -34.6037,
      "longitud": -58.3816,
      "descripcion": "CABA"
    },
    "fechaAcontecimiento": "2024-12-15T14:30:00",
    "etiquetas": [{ "nombre": "urgente" }],
    "contribuyente": null,
    "cuerpo": "Detalles completos del evento..."
  }'
```

---

#### 3. Modificar Hecho
```
PUT /api/dinamica/hechos/{id}
```
**Descripción**: Actualiza un hecho existente. Solo permite edición si el hecho tiene estado editable. Los hechos recientes (dentro del período editable) se pueden modificar.

**Parámetros**:
- `id` (Path): ID del hecho a modificar

**Request Body**: Mismo formato que la creación (HechoDTO completo)

**Response**: 
- Status: 200 OK
- Body: Objeto `Hecho` actualizado

**Ejemplo**:
```bash
curl -X PUT http://localhost:8089/api/dinamica/hechos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "textual",
    "titulo": "Robo - Información actualizada",
    "descripcion": "Nueva descripción",
    "categoria": { "detalle": "Robo" },
    "ubicacion": {
      "latitud": -34.6037,
      "longitud": -58.3816,
      "descripcion": "CABA"
    },
    "fechaAcontecimiento": "2024-12-15T14:30:00",
    "etiquetas": [{ "nombre": "urgente" }],
    "contribuyente": null,
    "cuerpo": "Contenido actualizado..."
  }'
```

---

#### 4. Cargar Contenido Multimedia
```
POST /api/dinamica/upload/{id}
```
**Descripción**: Carga una imagen o archivo multimedia para un hecho. El archivo se almacena en Supabase y se asocia al hecho.

**Parámetros**:
- `id` (Path): ID del hecho (debe ser de tipo "multimedia")
- `file` (Form Data): Archivo a cargar (MultipartFile)

**Content-Type**: multipart/form-data

**Response**: 
- Status: 200 OK
- Body: (vacío)

**Ejemplo con curl**:
```bash
curl -X POST http://localhost:8089/api/dinamica/upload/1 \
  -F "file=@./imagen.jpg"
```

**Ejemplo con JavaScript/Fetch**:
```javascript
const formData = new FormData();
formData.append('file', fileInput.files[0]);

fetch('http://localhost:8089/api/dinamica/upload/1', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.catch(error => console.error('Error:', error));
```

---

### Solicitudes de Eliminación

#### 1. Obtener Solicitudes
```
GET /api/dinamica/solicitudes
```
**Descripción**: Obtiene todas las solicitudes de eliminación registradas.

**Response**: 
- Status: 200 OK
- Body: Lista de objetos `SolicitudEliminacion`

```json
[
  {
    "id": 1,
    "hecho": { "id": 5, "titulo": "..." },
    "justificacion": "Contenido inapropiado",
    "estado": "PENDIENTE",
    "fechaCreacion": "2024-12-15T14:30:00"
  }
]
```

---

#### 2. Crear Solicitud de Eliminación
```
POST /api/dinamica/solicitudes
```
**Descripción**: Crea una nueva solicitud para eliminar un hecho. El contenido se analiza automáticamente para detectar spam. Si se detecta spam, la solicitud se rechaza automáticamente.

**Request Body** (SolicitudDTO):
```json
{
  "idHecho": 5,
  "justificacion": "Este contenido es inapropiado porque..."
}
```

**Campos Obligatorios**:
- `idHecho`: ID del hecho a eliminar
- `justificacion`: Razón por la que se solicita la eliminación

**Detección de Spam**:
- El módulo analiza la justificación usando un corpus de palabras clave
- Si se detecta spam, el estado se establece automáticamente a "RECHAZADA"
- Se registra en logs: "Solicitud de eliminacion rechazada por detectar spam en la justificacion."

**Response**: 
- Status: 201 Created
- Body: `Integer` (ID de la solicitud creada)

**Ejemplo**:
```bash
curl -X POST http://localhost:8089/api/dinamica/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "idHecho": 5,
    "justificacion": "Este contenido es potencialmente peligroso y debe ser revisado"
  }'
```

---

#### 3. Actualizar Estado de Solicitud
```
PUT /api/dinamica/solicitudes/{id}
```
**Descripción**: Modifica el estado de una solicitud de eliminación. Solo administradores pueden cambiar estados.

**Parámetros**:
- `id` (Path): ID de la solicitud

**Request Body** (Enum `Estado_Solicitud`):
```json
"ACEPTADA"
```

**Estados Posibles**:
- `PENDIENTE`: Solicitud registrada, esperando revisión
- `ACEPTADA`: Solicitud aprobada (el hecho será ocultado)
- `RECHAZADA`: Solicitud rechazada (el hecho permanece visible)

**Response**: 
- Status: 200 OK
- Body: Objeto `SolicitudEliminacion` actualizado

**Ejemplo**:
```bash
curl -X PUT http://localhost:8089/api/dinamica/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '"ACEPTADA"'
```

---

## Modelos de Datos

### HechoDTO (Request)
```json
{
  "tipo": "textual|multimedia",
  "titulo": "Título del hecho",
  "descripcion": "Descripción breve",
  "categoria": {
    "id": 1,
    "detalle": "Robo"
  },
  "ubicacion": {
    "id": 1,
    "latitud": -34.6037,
    "longitud": -58.3816,
    "descripcion": "Ubicación textual"
  },
  "fechaAcontecimiento": "2024-12-15T14:30:00",
  "fechaCarga": "2024-12-15T15:00:00",
  "etiquetas": [
    {
      "id": 1,
      "nombre": "violencia"
    }
  ],
  "contribuyente": {
    "id": 1,
    "cloakId": "uuid-del-usuario",
    "nombre": "Usuario X"
  },
  "cuerpo": "Contenido textual detallado...",
  "contenidoMultimedia": ["imagen1.jpg", "video1.mp4"]
}
```

### Hecho (Response)
```json
{
  "id": 1,
  "titulo": "Robo en San Isidro",
  "descripcion": "Se reportó un robo",
  "categoria": {
    "id": 1,
    "detalle": "Robo"
  },
  "ubicacion": {
    "id": 1,
    "latitud": -34.4835,
    "longitud": -58.5249,
    "descripcion": "San Isidro"
  },
  "fechaAcontecimiento": "2024-12-15T14:30:00",
  "fechaCarga": "2024-12-15T15:00:00",
  "origenFuente": {
    "id": 1,
    "nombre": "Dinámica",
    "url": "http://localhost:8082"
  },
  "etiquetas": [
    { "id": 1, "nombre": "violencia" }
  ],
  "contribuyente": {
    "id": 1,
    "nombre": "Anonimo"
  },
  "cuerpo": "Detalles del evento...",
  "esEditable": true
}
```

### SolicitudEliminacion
```json
{
  "id": 1,
  "hecho": {
    "id": 5,
    "titulo": "Evento a eliminar"
  },
  "justificacion": "Contenido inapropiado",
  "estado": "PENDIENTE",
  "fechaCreacion": "2024-12-15T14:30:00"
}
```

---

## Sistema de Detección de Spam

### Funcionamiento

El módulo incluye un detector automático de spam que analiza el contenido de las solicitudes de eliminación antes de aceptarlas.

### Componentes

#### `DetectorDeSpam` (Interface)
- Define el método estático `esSpam(String texto)` para detectar si un texto contiene spam
- Utiliza un `AlgoritmoSpam` integrado

#### `AlgoritmoSpam`
- Implementa el algoritmo de detección usando un corpus de palabras clave
- Lee palabras sospechosas de `corpus.txt`
- Normaliza el texto (elimina acentos, convierte a minúsculas)
- Verifica si palabras del texto coinciden con el corpus

#### `LectorCorpus`
- Lee el archivo `corpus.txt` desde el classpath
- Proporciona lista de palabras clave para identificar spam

### Corpus de Spam

El archivo `src/main/resources/corpus.txt` contiene palabras/frases clave que se consideran spam:
```
casino
dinero fácil
viagra
compra ahora
...
```

### Ejemplo

```java
// Si la justificación contiene palabras del corpus
String justificacion = "Gana dinero fácil ahora mismo";
boolean esSpam = DetectorDeSpam.esSpam(justificacion); // true

// La solicitud se rechaza automáticamente
nuevaSolicitudEliminacion.setEstado(Estado_Solicitud.RECHAZADA);
```

---

## Validación de Datos

### Validación de HechoDTO

Se valida automáticamente en los endpoints POST y PUT:

**Campos Obligatorios (todos)**:
- ✅ `tipo`: Debe ser "textual" o "multimedia"
- ✅ `titulo`: No puede ser nulo
- ✅ `descripcion`: No puede ser nulo
- ✅ `categoria`: No puede ser nulo
- ✅ `ubicacion`: No puede ser nulo
- ✅ `fechaAcontecimiento`: No puede ser nulo
- ✅ `etiquetas`: No puede ser nulo

**Campos Condicionales**:
- Para tipo **"textual"**: `cuerpo` es obligatorio
- Para tipo **"multimedia"**: `contenidoMultimedia` es obligatorio

Si algún campo falta, se devuelve error:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Campo obligatorio faltante: cuerpo"
}
```

---

## Integración con Supabase

### Configuración

Los archivos multimedia se almacenan en Supabase usando:
- **URL**: `${SUPABASE_URL}` (ej: https://your-project.supabase.co)
- **Bucket**: `${SUPABASE_BUCKET}` (ej: metamapa-bucket)
- **Service Key**: `${SUPABASE_SERVICE_KEY}` (clave de servicio)

### Flujo de Almacenamiento

1. Usuario crea hecho de tipo "multimedia"
2. Usuario sube archivo mediante `/upload/{id}`
3. El archivo se carga a Supabase
4. Se obtiene el nombre del archivo almacenado
5. El nombre se añade a la lista `contenidoMultimedia` del hecho
6. Se guarda en base de datos

### URLs de Archivos

Una vez almacenados, los archivos son accesibles en:
```
https://your-project.supabase.co/storage/v1/object/public/metamapa-bucket/filename
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
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

---

### Métricas de Prometheus
```
GET /actuator/prometheus
```

Devuelve métricas incluyendo:
- Latencia de endpoints
- Número de solicitudes por endpoint
- Errores por tipo
- Información de la JVM

---

### Logs

Los logs se escriben en `/var/log/dinamica/dinamica.log` con información sobre:
- Hechos creados: "Hecho creado con exito, ID: {id}"
- Solicitudes creadas: "Solicitud de eliminacion creada con exito, ID: {id}"
- Spam detectado: "Solicitud de eliminacion rechazada por detectar spam..."
- Errores y excepciones

---

## Ejemplos de Uso Completo

### Ejemplo 1: Crear y Cargar Hecho Multimedia

```bash
# 1. Crear hecho multimedia
HECHO_ID=$(curl -X POST http://localhost:8089/api/dinamica/hechos \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "multimedia",
    "titulo": "Video de incidente",
    "descripcion": "Grabación del evento",
    "categoria": { "detalle": "Incidente" },
    "ubicacion": {
      "latitud": -34.6037,
      "longitud": -58.3816,
      "descripcion": "Buenos Aires"
    },
    "fechaAcontecimiento": "2024-12-15T10:00:00",
    "etiquetas": [{ "nombre": "seguridad" }],
    "contribuyente": null,
    "contenidoMultimedia": []
  }')

echo "Hecho creado con ID: $HECHO_ID"

# 2. Cargar archivo
curl -X POST http://localhost:8089/api/dinamica/upload/$HECHO_ID \
  -F "file=@./video.mp4"

echo "Archivo cargado exitosamente"
```

---

### Ejemplo 2: Crear y Filtrar Hechos

```bash
# 1. Crear varios hechos
curl -X POST http://localhost:8089/api/dinamica/hechos \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "textual",
    "titulo": "Robo reportado",
    "descripcion": "Robo en zona céntrica",
    "categoria": { "detalle": "Robo" },
    "ubicacion": {
      "latitud": -34.6037,
      "longitud": -58.3816,
      "descripcion": "Centro"
    },
    "fechaAcontecimiento": "2024-12-15T14:00:00",
    "etiquetas": [{ "nombre": "urgente" }],
    "contribuyente": null,
    "cuerpo": "Detalles del robo..."
  }'

# 2. Filtrar por categoría
curl "http://localhost:8089/api/dinamica/hechos?categoria=Robo"

# 3. Filtrar por ubicación
curl "http://localhost:8089/api/dinamica/hechos?latitud=-34.6037&longitud=-58.3816"

# 4. Filtrar por rango de fechas
curl "http://localhost:8089/api/dinamica/hechos?fecha_reporte_desde=2024-12-15T00:00:00&fecha_reporte_hasta=2024-12-16T00:00:00"
```

---

### Ejemplo 3: Solicitud de Eliminación

```bash
# 1. Crear solicitud válida
curl -X POST http://localhost:8089/api/dinamica/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "idHecho": 1,
    "justificacion": "Este contenido viola las políticas de seguridad"
  }'

# 2. Crear solicitud que será rechazada por spam
curl -X POST http://localhost:8089/api/dinamica/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "idHecho": 2,
    "justificacion": "Gana dinero fácil comprando ahora casino online"
  }'
# Será rechazada automáticamente

# 3. Ver solicitudes
curl http://localhost:8089/api/dinamica/solicitudes

# 4. Aceptar solicitud (requiere token de admin)
curl -X PUT http://localhost:8089/api/dinamica/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '"ACEPTADA"'
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
java -jar target/dinamica-0.0.1-SNAPSHOT.jar
```

---

## Resolución de Problemas

### Error: "Hecho no encontrado con ID"
**Causa**: El ID proporcionado no existe
**Solución**: Verificar que el hecho existe antes de editarlo o cargar archivos

### Error: "Campo obligatorio faltante"
**Causa**: Falta un campo requerido en el request
**Solución**: Verificar la documentación de campos obligatorios para el tipo de hecho

### Error al cargar archivo
**Causa**: Problema de conexión con Supabase o credenciales inválidas
**Solución**: Verificar `SUPABASE_URL`, `SUPABASE_BUCKET` y `SUPABASE_SERVICE_KEY`

### Error de Base de Datos
**Causa**: Conexión a MySQL fallida
**Solución**: Verificar `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`

---

## Integración con Otros Módulos

- **Agregador**: Los hechos creados en Dinámica se sincronizan con el Agregador
- **Gestor de Personas**: Información de contribuyentes
- **Estadísticas**: Consume datos de hechos para análisis
- **API Gateway**: Punto de entrada único para todas las solicitudes

---

## Seguridad

- 🔐 Validación de entrada en todos los endpoints
- 🚫 Detección automática de spam en solicitudes
- 📝 Logs detallados de acciones
- 🔍 Validación de campos obligatorios
- 🌐 Integración con OAuth2 (a través del API Gateway)


