<div align="center">
<h1> Metamapa </h1>

Plataforma colaborativa de mapeo de hechos.

_Proyecto universitario desarrollado para la materia Diseño de Sistemas de Informacion de la Universidad Tecnologica Nacional FRBA._
</div>

Metamapa es una plataforma que permite visualizar (a traves de una interfaz web) acontecimientos reportados en distintos puntos geograficos, con informacion relevante relacionada a cada uno de ellos.

![Metamapa](/readmes/images/map.png "metamapa")

La informacion es obtenida de diferentes fuentes de datos:
- Datos estaticos leidos directamente de archivos con reportes sobre distintos hechos
- Integracion con otras organizaciones dispuestas a ofrecer datos relevantes para el sitio
- Hechos reportados directamente por usuarios a traves del sitio web

<p>Para mas informacion sobre la arquitectura utilizada ingresar a la <a href=https://drive.google.com/drive/folders/1NGHmyN9-gsV16knxnLY5uJKrNReFTrPV?hl=es_419>documentacion</a>.</p>

## Features Principales
### Crear hechos
Como usuario, puedo crear un nuevo hecho para ser visualizado en el sistema. A traves de un formulario en la pagina, se debe agregar la informacion correspondiente (categoria, fecha, lugar del acontecimiento, etc) y, dependiendo el formato elegido, un texto explicando el hecho o bien contenido multimedia que lo respalde.

![crear-hecho](/readmes/images/upload-hecho.png "crear-hecho")

### Reportar hechos
Los usuarios pueden ver los detalles de cada hecho subido al sistema. Si se considera que presentan informacion erronea o ambigua, el hecho puede ser reportado (con una breve explicacion), para que un usuario administrador lo revise y decida si debe o no ser eliminado o modificado.

![reportar-hecho](/readmes/images/report-hecho.png "reportar-hecho")

### Crear colecciones
Los hechos pueden ser agrupados en colecciones con la finalidad de ordenarlos segun aspectos especificos que tengan en comun. Estas colecciones solo pueden ser creadas por los administradores.

![coleccion](/readmes/images/coleccion.png "coleccion")

### Visualizar estadisticas
Los administradores pueden visualizar estadisticas relacionadas especificamente con el modelo de negocio. Ademas, estos datos pueden ser exportados segun se requiera.

![estadisticas](/readmes/images/stats.png "estadisticas")

### Observabilidad
Con el fin de monitorear el estado interno del sistema, se puede acceder a la interfaz grafica de grafana, que permitira ver metricas de cada modulo del sistema y distintos logs informativos de los mismos.

![grafana](/readmes/images/grafana.png "grafana")

## Rama "Local"
En esta rama se podra testear la aplicacion sin la necesidad de realizar un DEPLOY en un servicio externo. Cuenta con un README con las indicaciones para iniciar cada modulo de manera correcta.

## Creacion bases de datos
La carpeta mysql-init cuenta con un script que crea las bases de datos, este script es utilizado por el contenedor de MYSQL 

## Datos de prueba
La carpeta de json-prueba cuenta con archivos JSON en caso que se quiera utilizar Postman para ejecutar requests 

## Documentacion API
Cada carpeta de un modulo contiene un README el cual indica la documentacion de los endpoints que expone el modulo.
