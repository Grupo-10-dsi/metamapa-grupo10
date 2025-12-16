# Resumen de Cambios - API Agregador Frontend

## ✅ Métodos Actualizados

### 1. `obtenerColecciones({ page, size })`

**Antes:**
```javascript
async obtenerColecciones() {
    const response = await this.axiosInstance.get('/colecciones')
    return response.data
}
```

**Después:**
```javascript
async obtenerColecciones({ page = 0, size = 10 } = {}) {
    const response = await this.axiosInstance.get('/colecciones', {
        params: {
            page: page,
            size: size
        }
    })
    return response.data
}
```

**Cambios:**
- ✅ Acepta objeto con parámetros `page` y `size` (opcionales)
- ✅ Valores por defecto: `page=0`, `size=10`
- ✅ Envía parámetros en la query string al backend
- ✅ Retorna `PageResponse` del backend

**Ejemplo de uso:**
```javascript
// Valores por defecto
const response = await apiAgregador.obtenerColecciones();

// Con paginación específica
const response = await apiAgregador.obtenerColecciones({ page: 1, size: 20 });
```

---

### 2. `getHechosPorColeccion(id, filtros, consenso, page, size)`

**Antes:**
```javascript
async getHechosPorColeccion(id, filtros, consenso) {
    const params = {
        ...cleanFiltros,
        tipoNavegacion: consenso ? 'curada' : 'irrestricta'
    };
    // ...
}
```

**Después:**
```javascript
async getHechosPorColeccion(id, filtros, consenso, page = 0, size = 10) {
    const params = {
        ...cleanFiltros,
        tipoNavegacion: consenso ? 'curada' : 'irrestricta',
        page: page,
        size: size
    };
    // ...
}
```

**Cambios:**
- ✅ Acepta dos nuevos parámetros opcionales: `page` y `size`
- ✅ Valores por defecto: `page=0`, `size=10`
- ✅ Incluye parámetros de paginación en la request al backend
- ✅ Retorna `PageResponse` del backend

**Ejemplo de uso:**
```javascript
const filtros = { categoria: 'robo' };

// Valores por defecto
const response = await apiAgregador.getHechosPorColeccion(1, filtros, true);

// Con paginación específica
const response = await apiAgregador.getHechosPorColeccion(1, filtros, true, 0, 25);

// Segunda página
const response = await apiAgregador.getHechosPorColeccion(1, filtros, true, 1, 25);
```

---

## 📊 Estructura de Respuesta

Ambos métodos ahora retornan un objeto `PageResponse` con esta estructura:

```javascript
{
  content: [
    // Array de objetos (Colecciones o Hechos)
  ],
  pageNumber: 0,           // Página actual (base 0)
  pageSize: 10,            // Tamaño de página
  totalElements: 150,      // Total de elementos disponibles
  totalPages: 15,          // Total de páginas
  first: true,             // true si es la primera página
  last: false              // true si es la última página
}
```

---

## 🔄 Migración de Código Existente

### Obtener Colecciones

**Código Anterior:**
```javascript
const colecciones = await apiAgregador.obtenerColecciones();
// colecciones es un Array directamente
```

**Código Actualizado:**
```javascript
const response = await apiAgregador.obtenerColecciones();
const colecciones = response.content;
const totalColecciones = response.totalElements;
```

### Obtener Hechos de Colección

**Código Anterior:**
```javascript
const hechos = await apiAgregador.getHechosPorColeccion(1, {}, true);
// hechos es un Array directamente
```

**Código Actualizado:**
```javascript
const response = await apiAgregador.getHechosPorColeccion(1, {}, true);
const hechos = response.content;
const totalHechos = response.totalElements;
```

---

## 💡 Recomendaciones

### 1. Actualizar Componentes React

Los componentes que usan estos métodos deben actualizarse para manejar la nueva estructura de respuesta:

```javascript
// Antes
const [colecciones, setColecciones] = useState([]);
const data = await apiAgregador.obtenerColecciones();
setColecciones(data);

// Ahora
const [colecciones, setColecciones] = useState([]);
const [totalColecciones, setTotalColecciones] = useState(0);
const response = await apiAgregador.obtenerColecciones();
setColecciones(response.content);
setTotalColecciones(response.totalElements);
```

### 2. Implementar Controles de Paginación

Usa la metadata de la respuesta para crear controles de navegación:

```javascript
const [pageInfo, setPageInfo] = useState({
  pageNumber: 0,
  totalPages: 0,
  first: true,
  last: false
});

// Botón "Anterior" deshabilitado si first === true
<button disabled={pageInfo.first} onClick={...}>Anterior</button>

// Botón "Siguiente" deshabilitado si last === true
<button disabled={pageInfo.last} onClick={...}>Siguiente</button>
```

### 3. Mostrar Información al Usuario

```javascript
<p>
  Mostrando {response.content.length} de {response.totalElements} resultados
</p>
<p>
  Página {response.pageNumber + 1} de {response.totalPages}
</p>
```

---

## 🎯 Ventajas de los Cambios

1. **Mejor Rendimiento**: Solo se cargan los datos necesarios para cada página
2. **UX Mejorada**: Los usuarios pueden navegar grandes conjuntos de datos fácilmente
3. **Información Rica**: Metadata útil para mostrar al usuario
4. **Retrocompatible**: Los parámetros son opcionales, valores por defecto mantienen comportamiento básico
5. **Flexible**: Fácil ajustar el tamaño de página según necesidades

---

## 📝 Checklist de Migración

- [x] ✅ Método `obtenerColecciones` actualizado con paginación
- [x] ✅ Método `getHechosPorColeccion` actualizado con paginación
- [x] ✅ Parámetros con valores por defecto
- [x] ✅ Documentación creada (GUIA-USO-PAGINACION.md)
- [ ] ⏳ Actualizar componentes React que usan estos métodos
- [ ] ⏳ Implementar controles de paginación en UI
- [ ] ⏳ Probar funcionalidad con datos reales

---

## 🚀 Próximos Pasos

1. Identificar todos los componentes que usan `obtenerColecciones()` y `getHechosPorColeccion()`
2. Actualizar cada componente para manejar `PageResponse`
3. Implementar componentes de paginación reutilizables
4. Agregar estilos CSS para los controles de paginación
5. Probar navegación entre páginas
6. Probar cambios de tamaño de página

---

## 📚 Documentación Adicional

Ver archivos:
- `GUIA-USO-PAGINACION.md` - Guía completa con ejemplos de componentes React
- `agregador/PAGINACION-README.md` - Documentación del backend
- `agregador/RESUMEN-PAGINACION.md` - Resumen de cambios en el backend

