# Normalizador - Módulo de Normalización de Datos

## Descripción

El módulo **Normalizador** es responsable de estandarizar y normalizar información de hechos en la plataforma MetaMapa. Proporciona servicios para:

- 🏷️ **Normalizar categorías** de hechos a valores estándares
- 🔄 **Mapear variaciones** de categorías a términos canónicos
- 📝 **Validar y procesar** información de hechos
- 🔍 **Enriquecer datos** mediante equivalencias predefinidas
- ⚙️ **Pipeline de transformación** extensible

## Características Principales

- 📋 **Normalización de Categorías**: Mapeo de variaciones a categorías estándar
- 🔀 **Pipeline Extensible**: Arquitectura modular con múltiples normalizadores
- 📖 **Diccionario de Equivalencias**: Basado en archivo JSON configurable
- 🎯 **Mapeo Inteligente**: Busca equivalencias y agrega nuevas dinámicamente
- 🔧 **Fácil de Extender**: Interfaz `INormalizador` para nuevos normalizadores
- ⚡ **Sin Base de Datos**: Funciona completamente en memoria
- 🌐 **REST API Simple**: Endpoint único para normalización

## Tecnología

- **Framework**: Spring Boot 3.5.5
- **Lenguaje**: Java 17
- **Procesamiento**: Stream API (reduce)
- **Configuración**: JSON (equivalencias.json)
- **API**: REST
- **Patrón**: Chain of Responsibility + Pipeline
- **Monitoreo**: Micrometer + Prometheus

## Configuración

### Variables de Entorno

No requiere variables de entorno. Funciona completamente de forma independiente.

```bash
# Puerto (por defecto)
SERVER_PORT=8087

# Logs
LOGGING_FILE_PATH=/var/log/normalizador
```

### Configuración Principal

- **Puerto**: 8087
- **Ruta Base**: `/normalizador`
- **Archivo de Equivalencias**: `src/main/resources/equivalencias.json`
- **Logs**: `/var/log/normalizador/spring.log`

---

## Diccionario de Equivalencias

### Ubicación
```
src/main/resources/equivalencias.json
```

### Estructura
Archivo JSON que mapea variaciones de categorías a categorías canónicas:

```json
{
  "incendio forestal": "INCENDIO_FORESTAL",
  "fuego forestal": "INCENDIO_FORESTAL",
  "quema de bosque": "INCENDIO_FORESTAL",
  "incendio en bosque": "INCENDIO_FORESTAL",
  
  "inundacion": "INUNDACION",
  "anegamiento": "INUNDACION",
  "desborde de rio": "INUNDACION",
  "crecida de agua": "INUNDACION",
  
  "terremoto": "SISMO",
  "sismo": "SISMO",
  "temblor": "SISMO",
  "movimiento telurico": "SISMO",
  
  "accidente vehicular": "ACCIDENTE_TRANSITO",
  "choque": "ACCIDENTE_TRANSITO",
  "colision vehicular": "ACCIDENTE_TRANSITO",
  "accidente de transito": "ACCIDENTE_TRANSITO",
  
  "tornado": "EVENTO_CLIMATICO_EXTREMO",
  "huracan": "EVENTO_CLIMATICO_EXTREMO",
  "tormenta severa": "EVENTO_CLIMATICO_EXTREMO",
  "granizada": "EVENTO_CLIMATICO_EXTREMO",
  
  "derrame quimico": "INCIDENTE_AMBIENTAL",
  "contaminacion": "INCIDENTE_AMBIENTAL",
  "vertido toxico": "INCIDENTE_AMBIENTAL",
  
  "corte de energia": "FALLA_SERVICIO_PUBLICO",
  "corte de agua": "FALLA_SERVICIO_PUBLICO",
  "interrupcion de suministro": "FALLA_SERVICIO_PUBLICO"
}
```

### Categorías Estándar Soportadas

| Categoría Estándar | Variaciones |
|-------------------|-----------|
| **INCENDIO_FORESTAL** | incendio forestal, fuego forestal, quema de bosque, incendio en bosque |
| **INUNDACION** | inundacion, anegamiento, desborde de rio, crecida de agua |
| **SISMO** | terremoto, sismo, temblor, movimiento telurico |
| **ACCIDENTE_TRANSITO** | accidente vehicular, choque, colision vehicular, accidente de transito |
| **EVENTO_CLIMATICO_EXTREMO** | tornado, huracan, tormenta severa, granizada |
| **INCIDENTE_AMBIENTAL** | derrame quimico, contaminacion, vertido toxico |
| **FALLA_SERVICIO_PUBLICO** | corte de energia, corte de agua, interrupcion de suministro |

---

## Endpoints REST API

### Normalizar Hecho

```
PATCH /normalizador/normalizar
```

**Descripción**: Normaliza un hecho aplicando una cadena de transformadores (pipeline). Actualmente normaliza categorías mapeándolas a valores estándares.

**Request Body** (HechoDTO):
```json
{
  "id": 1,
  "titulo": "Incendio forestal en Córdoba",
  "descripcion": "Fuego descontrolado en zona boscosa",
  "categoria": {
    "detalle": "fuego forestal"
  },
  "ubicacion": {
    "latitud": -31.420083,
    "longitud": -64.188776,
    "descripcion": "Córdoba, Argentina"
  },
  "fechaAcontecimiento": "2024-12-15T14:30:00",
  "fechaCarga": "2024-12-15T15:00:00",
  "etiquetas": [
    {
      "nombre": "emergencia"
    }
  ],
  "origenFuente": null,
  "contenidoMultimedia": null,
  "cuerpo": null,
  "contribuyente": null
}
```

**Parámetros**:
- `categoria.detalle` (obligatorio): Categoría a normalizar

**Response**: 
- Status: 200 OK
- Body: `HechoDTO` normalizado

**Respuesta normalizada**:
```json
{
  "id": 1,
  "titulo": "Incendio forestal en Córdoba",
  "descripcion": "Fuego descontrolado en zona boscosa",
  "categoria": {
    "detalle": "INCENDIO_FORESTAL"
  },
  "ubicacion": {
    "latitud": -31.420083,
    "longitud": -64.188776,
    "descripcion": "Córdoba, Argentina"
  },
  "fechaAcontecimiento": "2024-12-15T14:30:00",
  "fechaCarga": "2024-12-15T15:00:00",
  "etiquetas": [
    {
      "nombre": "emergencia"
    }
  ],
  "origenFuente": null,
  "contenidoMultimedia": null,
  "cuerpo": null,
  "contribuyente": null
}
```

**Ejemplos**:

#### Ejemplo 1: Normalizar categoría conocida
```bash
curl -X PATCH http://localhost:8087/normalizador/normalizar \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "titulo": "Incendio",
    "descripcion": "Fuego en bosque",
    "categoria": { "detalle": "fuego forestal" },
    "ubicacion": { "latitud": -31.42, "longitud": -64.18 },
    "fechaAcontecimiento": "2024-12-15T14:30:00",
    "etiquetas": []
  }' | jq '.'
```

**Respuesta**:
```json
{
  "categoria": {
    "detalle": "INCENDIO_FORESTAL"
  }
}
```

#### Ejemplo 2: Normalizar categoría desconocida
```bash
curl -X PATCH http://localhost:8087/normalizador/normalizar \
  -H "Content-Type: application/json" \
  -d '{
    "id": 2,
    "titulo": "Evento extraño",
    "descripcion": "Categoría nueva",
    "categoria": { "detalle": "evento desconocido" },
    "ubicacion": { "latitud": 0, "longitud": 0 },
    "fechaAcontecimiento": "2024-12-15T14:30:00",
    "etiquetas": []
  }' | jq '.categoria'
```

**Respuesta** (agrega automáticamente como nueva categoría):
```json
{
  "detalle": "evento desconocido"
}
```

---

### Health Check

```
GET /normalizador/health
```

**Descripción**: Verifica que el servicio de normalización está activo.

**Response**: 
- Status: 200 OK
- Body: `"Servicio de normalizacion activo"`

**Ejemplo**:
```bash
curl http://localhost:8087/normalizador/health
```

**Respuesta**:
```
Servicio de normalizacion activo
```

---

## Modelos de Datos

### HechoDTO

DTO que encapsula toda la información de un hecho para normalizar:

```java
public class HechoDTO {
    private Integer id;                              // ID del hecho
    private String titulo;                           // Título
    private String descripcion;                      // Descripción
    private Categoria categoria;                     // **Normalizable**
    private Ubicacion ubicacion;                     // Ubicación
    private LocalDateTime fechaAcontecimiento;       // Fecha del evento
    private LocalDateTime fechaCarga;                // Fecha de registro
    private Origen_Fuente origenFuente;             // Origen (ESTATICA, DINAMICA, etc.)
    private List<Etiqueta> etiquetas;               // Etiquetas
    private List<String> contenidoMultimedia;       // Archivos multimedia
    private String cuerpo;                          // Contenido textual
    private Contribuyente contribuyente;            // Reportante
}
```

### Categoria

```java
public class Categoria {
    private String detalle;  // Nombre/código de la categoría
}
```

**Valores de entrada (variaciones)**:
- "incendio forestal", "fuego forestal", "quema de bosque"
- "inundacion", "anegamiento", "desborde de rio"
- "terremoto", "sismo", "temblor"
- etc.

**Valores de salida (normalizados)**:
- "INCENDIO_FORESTAL", "INUNDACION", "SISMO", etc.

---



### Implementación

```java
public HechoDTO normalizar(HechoDTO hechoCrudo) {
    HechoDTO hechoNormalizado = normalizadores.stream()
        .reduce(
            hechoCrudo,
            (hechoAcumulado, unNormalizador) -> unNormalizador.normalizar(hechoAcumulado),
            (hecho1, hecho2) -> hecho2
        );
    return hechoNormalizado;
}
```

**Flujo**:
1. Comienza con `hechoCrudo` como acumulador
2. Aplica cada `INormalizador` en secuencia
3. El resultado de uno es entrada del siguiente
4. Devuelve el resultado final normalizador

---

## Casos de Uso

### Caso 1: Normalización Exitosa

**Entrada**:
```json
{
  "categoria": { "detalle": "terremoto" }
}
```

**Proceso**:
1. NormalizadorCategorias busca "terremoto" en equivalencias.json
2. Encuentra mapeo: "terremoto" → "SISMO"
3. Reemplaza categoria.detalle por "SISMO"

**Salida**:
```json
{
  "categoria": { "detalle": "SISMO" }
}
```

---

### Caso 2: Categoría Desconocida

**Entrada**:
```json
{
  "categoria": { "detalle": "avalancha de nieve" }
}
```

**Proceso**:
1. NormalizadorCategorias busca "avalancha de nieve" en equivalencias.json
2. No encuentra mapeo
3. Agrega "avalancha de nieve" como nueva categoría al mapa
4. Retorna con la misma categoría (sin normalizar)

**Salida**:
```json
{
  "categoria": { "detalle": "avalancha de nieve" }
}
```

**Nota**: La nueva categoría queda registrada para futuras normalizaciones.

---

### Caso 3: Variaciones de la Misma Categoría

**Entrada 1**:
```json
{ "categoria": { "detalle": "incendio forestal" } }
```
→ Normaliza a **INCENDIO_FORESTAL**

**Entrada 2**:
```json
{ "categoria": { "detalle": "fuego forestal" } }
```
→ Normaliza a **INCENDIO_FORESTAL**

**Entrada 3**:
```json
{ "categoria": { "detalle": "quema de bosque" } }
```
→ Normaliza a **INCENDIO_FORESTAL**

Todas convergen a la misma categoría estándar.

---

## Extensión del Sistema

### Agregar Nuevo Normalizador

Para agregar un nuevo tipo de normalización:

#### 1. Crear clase que implemente INormalizador

```java
@Component
public class NormalizadorUbicaciones implements INormalizador {
    public HechoDTO normalizar(HechoDTO hecho) {
        // Lógica de normalización de ubicaciones
        if(hecho.getUbicacion() != null) {
            // Procesar ubicación
        }
        return hecho;
    }
}
```

#### 2. Registrar como @Component

Spring lo inyectará automáticamente en la lista de normalizadores.

#### 3. El Normalizador lo ejecutará automáticamente

El componente `Normalizador` detectará la nueva implementación y la incluirá en el pipeline.

---

## Ejemplo de Uso Completo

### Normalizar hecho con curl

```bash
curl -X PATCH http://localhost:8087/normalizador/normalizar \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Incendio registrado",
    "descripcion": "Fuego en zona boscosa",
    "categoria": { "detalle": "fuego forestal" },
    "ubicacion": {
      "latitud": -31.42,
      "longitud": -64.18,
      "descripcion": "Córdoba"
    },
    "fechaAcontecimiento": "2024-12-15T14:30:00",
    "etiquetas": [],
    "id": 1
  }' | jq '.categoria'
```

**Respuesta**:
```json
{
  "detalle": "INCENDIO_FORESTAL"
}
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

Devuelve métricas incluyendo:
- Latencia de normalización
- Número de hechos normalizados
- Información de la JVM

### Logs

Ubicación: `/var/log/normalizador/spring.log`

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
java -jar target/normalizador-0.0.1-SNAPSHOT.jar
```

---

## Integración con Otros Módulos

### Dinámica
Cuando se crea un hecho, puede llamar a normalizar:

```
1. Usuario crea hecho con categoría: "incendio forestal"
2. Dinámica llama a: PATCH /normalizador/normalizar
3. Recibe: categoría normalizada a "INCENDIO_FORESTAL"
4. Guarda hecho con categoría estándar
```

### Agregador
Para enriquecer búsquedas:

```
1. Usuario busca por categoría: "fuego"
2. Agregador llama a normalizar
3. Obtiene: "INCENDIO_FORESTAL"
4. Busca en BD por categoría normalizada
```

