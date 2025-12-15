# Documentación Swagger - MetaMapa

## Acceso al Swagger UI

El Swagger UI del módulo de estadística está disponible en:

```
https://metamapa.page/swagger-estadistica/swagger-ui.html
```

## Características

- **Documentación interactiva**: Puedes probar los endpoints directamente desde la interfaz
- **Esquemas de datos**: Visualiza los modelos de datos utilizados
- **Autenticación**: Algunos endpoints pueden requerir autenticación

## Endpoints Disponibles

El módulo de estadística proporciona endpoints para:
- Obtener estadísticas de colecciones
- Consultar datos agregados
- Generar reportes en formato CSV
- Analizar patrones de uso

## Uso

1. Accede a `https://metamapa.page/swagger-estadistica/swagger-ui.html`
2. Explora los endpoints disponibles en la interfaz
3. Para probar un endpoint:
   - Haz clic en el endpoint deseado
   - Haz clic en "Try it out"
   - Completa los parámetros requeridos
   - Haz clic en "Execute"

## Documentación OpenAPI

La especificación OpenAPI en formato JSON está disponible en:

```
https://metamapa.page/swagger-estadistica/api-docs
```

## Notas Técnicas

- El Swagger está expuesto a través del reverse proxy Nginx
- La configuración incluye headers apropiados para funcionamiento detrás de proxy
- Los endpoints de la API siguen siendo accesibles a través de `/api/` como antes

