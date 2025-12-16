# Guía de Uso - API Agregador con Paginación

## Métodos Actualizados

### 1. `obtenerColecciones({ page, size })`

Obtiene las colecciones del sistema con soporte de paginación.

**Parámetros:**
- `page` (opcional): Número de página (base 0). **Default: 0**
- `size` (opcional): Cantidad de elementos por página. **Default: 10**

**Retorno:**
```javascript
{
  content: [Array de Colecciones],
  pageNumber: 0,
  pageSize: 10,
  totalElements: 100,
  totalPages: 10,
  first: true,
  last: false
}
```

**Ejemplos de uso:**

```javascript
// Sin parámetros (usa valores por defecto: page=0, size=10)
const response = await apiAgregador.obtenerColecciones();
const colecciones = response.content;
console.log(`Total: ${response.totalElements}, Página ${response.pageNumber + 1} de ${response.totalPages}`);

// Con paginación específica
const response = await apiAgregador.obtenerColecciones({ page: 0, size: 20 });

// Segunda página
const response = await apiAgregador.obtenerColecciones({ page: 1, size: 20 });

// Solo especificar el tamaño de página
const response = await apiAgregador.obtenerColecciones({ size: 50 });
```

### 2. `getHechosPorColeccion(id, filtros, consenso, page, size)`

Obtiene los hechos de una colección específica con soporte de paginación.

**Parámetros:**
- `id` (requerido): ID de la colección
- `filtros` (requerido): Objeto con filtros de búsqueda
- `consenso` (requerido): Boolean - `true` para navegación curada, `false` para irrestricta
- `page` (opcional): Número de página (base 0). **Default: 0**
- `size` (opcional): Cantidad de elementos por página. **Default: 10**

**Retorno:**
```javascript
{
  content: [Array de Hechos],
  pageNumber: 0,
  pageSize: 10,
  totalElements: 200,
  totalPages: 20,
  first: true,
  last: false
}
```

**Ejemplos de uso:**

```javascript
const filtros = {
  categoria: 'robo',
  fecha_reporte_desde: '2024-01-01',
  fecha_reporte_hasta: '2024-12-31'
};

// Sin paginación explícita (usa valores por defecto)
const response = await apiAgregador.getHechosPorColeccion(
  1,        // ID de colección
  filtros,
  true      // Navegación curada
);
const hechos = response.content;

// Con paginación específica
const response = await apiAgregador.getHechosPorColeccion(
  1,        // ID de colección
  filtros,
  false,    // Navegación irrestricta
  0,        // Primera página
  25        // 25 elementos por página
);

// Navegar a la segunda página
const response = await apiAgregador.getHechosPorColeccion(
  1,
  filtros,
  true,
  1,        // Segunda página
  25
);
```

## Ejemplos de Componentes React

### Ejemplo 1: Lista de Colecciones con Paginación

```javascript
import React, { useState, useEffect } from 'react';
import apiAgregador from '../api/api-agregador';

function ListaColecciones() {
  const [colecciones, setColecciones] = useState([]);
  const [paginacion, setPaginacion] = useState({
    pageNumber: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0,
    first: true,
    last: false
  });
  const [loading, setLoading] = useState(false);

  const cargarColecciones = async (page = 0, size = 10) => {
    setLoading(true);
    try {
      const response = await apiAgregador.obtenerColecciones({ page, size });
      
      setColecciones(response.content);
      setPaginacion({
        pageNumber: response.pageNumber,
        pageSize: response.pageSize,
        totalElements: response.totalElements,
        totalPages: response.totalPages,
        first: response.first,
        last: response.last
      });
    } catch (error) {
      console.error('Error al cargar colecciones:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarColecciones();
  }, []);

  const handlePageChange = (newPage) => {
    cargarColecciones(newPage, paginacion.pageSize);
  };

  const handlePageSizeChange = (newSize) => {
    cargarColecciones(0, parseInt(newSize));
  };

  return (
    <div className="colecciones-container">
      <h1>Colecciones</h1>
      
      {/* Controles superiores */}
      <div className="controls">
        <select 
          value={paginacion.pageSize} 
          onChange={(e) => handlePageSizeChange(e.target.value)}
        >
          <option value="10">10 por página</option>
          <option value="20">20 por página</option>
          <option value="50">50 por página</option>
        </select>
        
        <span>Total: {paginacion.totalElements} colecciones</span>
      </div>

      {/* Lista de colecciones */}
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <ul>
          {colecciones.map(coleccion => (
            <li key={coleccion.id}>
              <h3>{coleccion.titulo}</h3>
              <p>{coleccion.descripcion}</p>
            </li>
          ))}
        </ul>
      )}

      {/* Controles de paginación */}
      <div className="pagination">
        <button 
          onClick={() => handlePageChange(0)}
          disabled={paginacion.first}
        >
          Primera
        </button>
        
        <button 
          onClick={() => handlePageChange(paginacion.pageNumber - 1)}
          disabled={paginacion.first}
        >
          ← Anterior
        </button>
        
        <span>
          Página {paginacion.pageNumber + 1} de {paginacion.totalPages}
        </span>
        
        <button 
          onClick={() => handlePageChange(paginacion.pageNumber + 1)}
          disabled={paginacion.last}
        >
          Siguiente →
        </button>
        
        <button 
          onClick={() => handlePageChange(paginacion.totalPages - 1)}
          disabled={paginacion.last}
        >
          Última
        </button>
      </div>
    </div>
  );
}

export default ListaColecciones;
```

### Ejemplo 2: Hechos de una Colección con Paginación y Filtros

```javascript
import React, { useState, useEffect } from 'react';
import apiAgregador from '../api/api-agregador';

function HechosColeccion({ coleccionId }) {
  const [hechos, setHechos] = useState([]);
  const [paginacion, setPaginacion] = useState({
    pageNumber: 0,
    pageSize: 15,
    totalElements: 0,
    totalPages: 0,
    first: true,
    last: false
  });
  const [filtros, setFiltros] = useState({
    categoria: '',
    fecha_reporte_desde: '',
    fecha_reporte_hasta: ''
  });
  const [consenso, setConsenso] = useState(true);
  const [loading, setLoading] = useState(false);

  const cargarHechos = async (page = 0, size = 15) => {
    setLoading(true);
    try {
      const response = await apiAgregador.getHechosPorColeccion(
        coleccionId,
        filtros,
        consenso,
        page,
        size
      );
      
      setHechos(response.content);
      setPaginacion({
        pageNumber: response.pageNumber,
        pageSize: response.pageSize,
        totalElements: response.totalElements,
        totalPages: response.totalPages,
        first: response.first,
        last: response.last
      });
    } catch (error) {
      console.error('Error al cargar hechos:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarHechos();
  }, [coleccionId, consenso]); // Recargar cuando cambie la colección o el tipo de navegación

  const handleFiltroChange = (campo, valor) => {
    setFiltros(prev => ({
      ...prev,
      [campo]: valor
    }));
  };

  const aplicarFiltros = () => {
    cargarHechos(0, paginacion.pageSize); // Volver a la primera página al filtrar
  };

  const handlePageChange = (newPage) => {
    cargarHechos(newPage, paginacion.pageSize);
  };

  return (
    <div className="hechos-coleccion-container">
      <h2>Hechos de la Colección</h2>

      {/* Controles de navegación */}
      <div className="tipo-navegacion">
        <label>
          <input 
            type="radio" 
            checked={consenso === true}
            onChange={() => setConsenso(true)}
          />
          Navegación Curada
        </label>
        <label>
          <input 
            type="radio" 
            checked={consenso === false}
            onChange={() => setConsenso(false)}
          />
          Navegación Irrestricta
        </label>
      </div>

      {/* Filtros */}
      <div className="filtros">
        <input 
          type="text"
          placeholder="Categoría"
          value={filtros.categoria}
          onChange={(e) => handleFiltroChange('categoria', e.target.value)}
        />
        <input 
          type="date"
          value={filtros.fecha_reporte_desde}
          onChange={(e) => handleFiltroChange('fecha_reporte_desde', e.target.value)}
        />
        <input 
          type="date"
          value={filtros.fecha_reporte_hasta}
          onChange={(e) => handleFiltroChange('fecha_reporte_hasta', e.target.value)}
        />
        <button onClick={aplicarFiltros}>Aplicar Filtros</button>
      </div>

      {/* Información de resultados */}
      <p>
        Mostrando {hechos.length} de {paginacion.totalElements} hechos
      </p>

      {/* Lista de hechos */}
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <div className="hechos-list">
          {hechos.map(hecho => (
            <div key={hecho.id} className="hecho-card">
              <h3>{hecho.titulo}</h3>
              <p>{hecho.descripcion}</p>
              <small>
                {hecho.categoria?.detalle} - {hecho.fechaAcontecimiento}
              </small>
            </div>
          ))}
        </div>
      )}

      {/* Paginación */}
      <div className="pagination">
        <button 
          onClick={() => handlePageChange(paginacion.pageNumber - 1)}
          disabled={paginacion.first}
        >
          ← Anterior
        </button>
        
        <span>
          Página {paginacion.pageNumber + 1} de {paginacion.totalPages}
        </span>
        
        <button 
          onClick={() => handlePageChange(paginacion.pageNumber + 1)}
          disabled={paginacion.last}
        >
          Siguiente →
        </button>
      </div>
    </div>
  );
}

export default HechosColeccion;
```

## Migración de Código Existente

### Antes (sin paginación):

```javascript
// Obtener colecciones
const colecciones = await apiAgregador.obtenerColecciones();
colecciones.forEach(col => console.log(col.titulo));

// Obtener hechos
const hechos = await apiAgregador.getHechosPorColeccion(1, {}, true);
hechos.forEach(h => console.log(h.titulo));
```

### Después (con paginación):

```javascript
// Obtener colecciones
const response = await apiAgregador.obtenerColecciones();
const colecciones = response.content;
console.log(`Total: ${response.totalElements}`);
colecciones.forEach(col => console.log(col.titulo));

// Obtener hechos
const response = await apiAgregador.getHechosPorColeccion(1, {}, true);
const hechos = response.content;
console.log(`Total: ${response.totalElements}`);
hechos.forEach(h => console.log(h.titulo));
```

## Notas Importantes

1. **Estructura de Respuesta**: Todos los métodos ahora devuelven un objeto `PageResponse` con la propiedad `content` que contiene los datos
2. **Retrocompatibilidad**: Los parámetros de paginación son opcionales, se usan valores por defecto si no se especifican
3. **Base Cero**: La numeración de páginas comienza en 0
4. **Filtros**: Los filtros se aplican antes de la paginación en el backend
5. **Metadata Útil**: Usa `first` y `last` para deshabilitar botones de navegación

## CSS de Ejemplo para Paginación

```css
.pagination {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
  justify-content: center;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.pagination button:hover:not(:disabled) {
  background: #f0f0f0;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination span {
  font-weight: 500;
}
```

