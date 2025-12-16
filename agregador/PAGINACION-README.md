# Documentación de Paginación - Colecciones y Hechos

## Resumen de Implementación

Se ha implementado paginación en los siguientes endpoints del `AgregadorController`:

### 1. Obtener Colecciones con Paginación
**Endpoint**: `GET /agregador/colecciones`

**Parámetros**:
- `page` (opcional): Número de página (base 0). Default: `0`
- `size` (opcional): Cantidad de elementos por página. Default: `10`

**Ejemplo de Request**:
```
GET /agregador/colecciones?page=0&size=20
```

**Respuesta**:
```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Colección 1",
      "descripcion": "...",
      ...
    },
    ...
  ],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

### 2. Obtener Hechos de una Colección con Paginación
**Endpoint**: `GET /agregador/colecciones/{id}/hechos`

**Parámetros**:
- `id` (path): ID de la colección
- `categoria` (opcional): Filtro por categoría
- `fecha_reporte_desde` (opcional): Fecha de reporte desde
- `fecha_reporte_hasta` (opcional): Fecha de reporte hasta
- `fecha_acontecimiento_desde` (opcional): Fecha de acontecimiento desde
- `fecha_acontecimiento_hasta` (opcional): Fecha de acontecimiento hasta
- `latitud` (opcional): Latitud para filtrar
- `longitud` (opcional): Longitud para filtrar
- `tipoNavegacion` (requerido): `"irrestricta"` o `"curada"`
- `page` (opcional): Número de página (base 0). Default: `0`
- `size` (opcional): Cantidad de elementos por página. Default: `10`

**Ejemplo de Request**:
```
GET /agregador/colecciones/1/hechos?tipoNavegacion=irrestricta&page=0&size=15
```

**Ejemplo con filtros**:
```
GET /agregador/colecciones/1/hechos?tipoNavegacion=curada&categoria=robo&page=1&size=25
```

**Respuesta**:
```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Hecho 1",
      "descripcion": "...",
      "categoria": {...},
      "ubicacion": {...},
      ...
    },
    ...
  ],
  "pageNumber": 0,
  "pageSize": 15,
  "totalElements": 200,
  "totalPages": 14,
  "first": true,
  "last": false
}
```

## Estructura de PageResponse

La clase `PageResponse<T>` contiene:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `content` | `List<T>` | Lista de elementos de la página actual |
| `pageNumber` | `int` | Número de la página actual (base 0) |
| `pageSize` | `int` | Tamaño de la página |
| `totalElements` | `long` | Total de elementos disponibles |
| `totalPages` | `int` | Total de páginas |
| `first` | `boolean` | `true` si es la primera página |
| `last` | `boolean` | `true` si es la última página |

## Uso desde el Frontend (JavaScript)

### Obtener Colecciones Paginadas

```javascript
// Sin paginación (usa valores por defecto: page=0, size=10)
const response = await apiAgregador.obtenerColecciones();

// Con paginación específica
const response = await apiAgregador.obtenerColecciones({ page: 0, size: 20 });

// Acceder a los datos
const colecciones = response.content;
const totalPaginas = response.totalPages;
const totalElementos = response.totalElements;

console.log(`Mostrando ${colecciones.length} de ${totalElementos} colecciones`);
```

### Obtener Hechos de una Colección Paginados

```javascript
const filtros = {
  categoria: 'robo',
  fecha_reporte_desde: '2024-01-01'
};

// Con paginación
const response = await apiAgregador.getHechosPorColeccion(
  1,              // id de la colección
  filtros,        // filtros
  true,           // consenso (curada=true, irrestricta=false)
  0,              // page
  20              // size
);

// Acceder a los datos
const hechos = response.content;
const esPrimeraPagina = response.first;
const esUltimaPagina = response.last;
```

## Ejemplo de Componente React con Navegación

```javascript
import React, { useState, useEffect } from 'react';
import apiAgregador from '../api/api-agregador';

function ListaColecciones() {
  const [colecciones, setColecciones] = useState([]);
  const [pageInfo, setPageInfo] = useState({
    currentPage: 0,
    pageSize: 10,
    totalPages: 0,
    totalElements: 0,
    first: true,
    last: false
  });

  const cargarColecciones = async (page = 0, size = 10) => {
    try {
      const response = await apiAgregador.obtenerColecciones({ page, size });
      
      setColecciones(response.content);
      setPageInfo({
        currentPage: response.pageNumber,
        pageSize: response.pageSize,
        totalPages: response.totalPages,
        totalElements: response.totalElements,
        first: response.first,
        last: response.last
      });
    } catch (error) {
      console.error('Error al cargar colecciones:', error);
    }
  };

  useEffect(() => {
    cargarColecciones();
  }, []);

  const handlePreviousPage = () => {
    if (!pageInfo.first) {
      cargarColecciones(pageInfo.currentPage - 1, pageInfo.pageSize);
    }
  };

  const handleNextPage = () => {
    if (!pageInfo.last) {
      cargarColecciones(pageInfo.currentPage + 1, pageInfo.pageSize);
    }
  };

  return (
    <div>
      <h1>Colecciones ({pageInfo.totalElements} total)</h1>
      
      <ul>
        {colecciones.map(coleccion => (
          <li key={coleccion.id}>{coleccion.titulo}</li>
        ))}
      </ul>

      <div className="pagination-controls">
        <button 
          onClick={handlePreviousPage}
          disabled={pageInfo.first}
        >
          ← Anterior
        </button>
        
        <span>
          Página {pageInfo.currentPage + 1} de {pageInfo.totalPages}
        </span>
        
        <button 
          onClick={handleNextPage}
          disabled={pageInfo.last}
        >
          Siguiente →
        </button>
      </div>
    </div>
  );
}

export default ListaColecciones;
```

## Notas Importantes

1. **Base Cero**: La numeración de páginas comienza en 0, no en 1
2. **Valores por Defecto**: Si no se especifican `page` y `size`, se usan 0 y 10 respectivamente
3. **Retrocompatibilidad**: El frontend debe manejar tanto respuestas paginadas como no paginadas
4. **Rendimiento**: La paginación se realiza en memoria, ideal para datasets moderados
5. **Filtros**: Los filtros se aplican antes de la paginación

## Mejoras Futuras Sugeridas

- Implementar paginación a nivel de base de datos para grandes volúmenes
- Agregar ordenamiento (sort) como parámetro adicional
- Implementar cursor-based pagination para mejor rendimiento
- Cache de páginas visitadas en el frontend

