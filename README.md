# Guía de Despliegue Local - MetaMapa

Este documento describe los pasos necesarios para deployear y ejecutar el proyecto MetaMapa en tu entorno local.

## Requisitos Previos

- Docker
- Docker Compose

## Pasos de Instalación

### 1. Levantar los Contenedores

Posiciónate en el directorio raíz del proyecto y ejecuta el siguiente comando:

```bash
docker compose up -d --build
```

Este comando construirá las imágenes necesarias y levantará todos los servicios en segundo plano.

### 2. Esperar la Inicialización de Keycloak

Es importante esperar a que Keycloak y el API Gateway estén completamente inicializados antes de continuar. Para monitorear el proceso de inicio, ejecuta:

```bash
docker compose logs -f api-gateway
```

Cuando veas que el API Gateway se ha conectado exitosamente a Keycloak, puedes proceder al siguiente paso. Presiona `Ctrl+C` para salir de los logs.

### 3. Acceder a la Aplicación

Una vez que los servicios estén en ejecución, accede a la aplicación a través de:

```
https://localhost
```

**Nota:** Tu navegador mostrará una advertencia de seguridad debido al certificado SSL autofirmado. Acepta el riesgo y continúa para acceder a la aplicación.

## Configuración de Usuario Administrador

Para crear un usuario administrador en el sistema, sigue estos pasos:

### 1. Acceder a la Consola de Administración de Keycloak

Navega a:

```
https://localhost/admin
```

Credenciales de acceso:
- **Usuario:** `admin`
- **Contraseña:** `admin`

### 2. Completar el Perfil

Al primer ingreso, el sistema solicitará completar información adicional (email y nombre). Puedes ingresar valores de prueba, ya que no son críticos para el funcionamiento.

### 3. Asignar Rol de Administrador

1. En la consola de Keycloak, selecciona el realm **MetaMapa**
2. Navega a la sección **Users**
3. Si no existe ningún usuario:
   - Crea un nuevo usuario con los datos deseados
4. Si ya existe un usuario:
   - Selecciona el usuario al que deseas otorgar permisos
5. Ve a la pestaña **Role Mapping**
6. En **Realm Roles**, asigna el rol `admin`
7. Guarda los cambios

El usuario ahora tendrá privilegios de administrador en la plataforma.

---

## Solución de Problemas

Si experimentas problemas durante el despliegue:

- **Verificar estado de contenedores:**
  ```bash
  docker compose ps
  ```

- **Revisar logs de un servicio específico:**
  ```bash
  docker compose logs <nombre-servicio>
  ```

- **Reiniciar los servicios:**
  ```bash
  docker compose restart
  ```

- **Detener todos los servicios:**
  ```bash
  docker compose down
  ```

- **Limpiar y reiniciar desde cero:**
  ```bash
  docker compose down -v
  docker compose up -d --build
  ```

## Servicios Disponibles

El proyecto incluye los siguientes servicios:

- **API Gateway:** Punto de entrada principal de la aplicación
- **Keycloak:** Sistema de autenticación y autorización
- **Agregador:** Servicio de agregación de datos
- **Normalizador:** Servicio de normalización de datos
- **Visualizador:** Frontend de la aplicación
- **Base de Datos:** MySQL para almacenamiento persistente
- **Monitoring:** Prometheus, Grafana y Loki para monitoreo

---

## Próximos Pasos

Una vez que la aplicación esté en funcionamiento:

1. Crea una cuenta de usuario en la interfaz web
2. Si necesitas permisos de administrador, sigue la sección de "Configuración de Usuario Administrador"
3. Explora las funcionalidades de la plataforma MetaMapa

¡Disfruta usando MetaMapa! 🗺️

