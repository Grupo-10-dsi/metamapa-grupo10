package ar.edu.utn.frba.ddsi.agregador.controllers;


import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Filtro;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.services.AgregadorService;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/agregador")
@RestController
public class AgregadorController {
    private final AgregadorService agregadorService;

    public AgregadorController(AgregadorService agregadorService) {
        this.agregadorService = agregadorService;
    }

    // <----------------- OPERACIONES CRUD SOBRE COLECCIONES ----------------->
    /**
     * Crea una nueva coleccion en el sistema a partir del DTO que recibe.
     * Si se crea correctamente, devuelve el ID de la coleccion creada.
     */
    @PostMapping("/colecciones")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Integer crearColeccion(@RequestBody ColeccionDTO coleccion) {
        return this.agregadorService.crearColeccion(coleccion);
    }

    /**
     * Devuelve una lista de colecciones dependiendo de los parametros enviados en la req.
     * Si no se envian parametros, devuelve todas las colecciones.
     */
    @GetMapping("/colecciones")
    public List<Coleccion> obtenerColecciones(){
        return this.agregadorService.obtenerColecciones();
    }

    @GetMapping("/categorias")
    public List<Categoria> obtenerCategorias(){
        return this.agregadorService.obtenerCategorias();
    }
    /**
     * Devuelve una coleccion en particular a partir de su ID.
     * Si no se encuentra la coleccion, devuelve un error 404.
     */
    @GetMapping("/colecciones/{id}")
    public Coleccion obtenerColeccion(@PathVariable Integer id){
        return this.agregadorService.obtenerColeccion(id);
    }

    /**
     * Elimina una coleccion del sistema a partir de su ID. No devuelve contenido.
     * Si no se encuentra la coleccion, devuelve un error 404.
     */
    @DeleteMapping("/colecciones/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void eliminarColeccion(@PathVariable Integer id) {
        this.agregadorService.eliminarColeccionPorId(id);
    }

    /**
     * Permite modificar una coleccion.
     * Recibe el ID de la coleccion a modificar y un DTO con los nuevos datos.
     */
//    @PutMapping("/colecciones/{id}")
//    @ResponseStatus(org.springframework.http.HttpStatus.OK)
//    public Coleccion modificarColeccion(@PathVariable Integer id, @RequestBody ColeccionDTO coleccionDTO) {
//        return this.agregadorService.actualizarColeccion(id, coleccionDTO);
//    }


     //Endpoint para modificar coleccion
    @PatchMapping("/colecciones/{id}")
    public Coleccion modificarColeccion(@PathVariable Integer id, @RequestBody ActualizacionColeccionDTO actualizacionColeccion) {

        if(actualizacionColeccion.getAlgoritmo_consenso() != null) {
            return this.agregadorService.modificarAlgoritmoConsenso(id, actualizacionColeccion.getAlgoritmo_consenso());
        } else if (actualizacionColeccion.getUrls_fuente() != null) {
            return this.agregadorService.modificarListaDeFuentes(id, actualizacionColeccion.getUrls_fuente());
        } else {
            throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar");
        }

    }


//    @PatchMapping("/colecciones/{id}")
//    public Coleccion modificarFuentes(@PathVariable Integer id, @RequestBody List<String> urls_fuente) {
//        return this.agregadorService.modificarListaDeFuentes(id, urls_fuente);
//    }

    // Navegacion de forma irrestricta -> No se aplica curacion

    @GetMapping("/colecciones/{id}/hechos")
    public List<Hecho> obtenerHechosPorColeccion(@PathVariable Integer id,
                @RequestParam(required = false) String categoria,
                @RequestParam(required = false) String fecha_reporte_desde,
                @RequestParam(required = false) String fecha_reporte_hasta,
                @RequestParam(required = false) String fecha_acontecimiento_desde,
                @RequestParam(required = false) String fecha_acontecimiento_hasta,
                @RequestParam(required = false) Double latitud,
                @RequestParam(required = false) Double longitud,
                @RequestParam String tipoNavegacion) {

        var filtros = new Filtro(
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                latitud,
                longitud
        );

        if(tipoNavegacion == null) {
            throw new IllegalArgumentException("El tipo de navegacion es obligatorio");
        }

        if(tipoNavegacion.equals("irrestricta")) {
            return this.agregadorService.encontrarHechosPorColeccion(id, filtros);
        } else if (tipoNavegacion.equals("curada")) {
            return this.agregadorService.obtenerHechosCurados(id, filtros);
        } else {
            throw new IllegalArgumentException("Tipo de navegacion no soportado: " + tipoNavegacion);
        }
    }


    @GetMapping("/solicitudes")
    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return agregadorService.encontrarSolicitudes();
    }


    // Endpoint para generar solicitudes de eliminacion de hechos le pega metamapa
    @PostMapping("/solicitudes")
    public Integer generarSolicitudEliminacion(@RequestBody SolicitudDTO solicitudEliminacion) {
        return agregadorService.crearSolicitudEliminacion(solicitudEliminacion);
    }

    // Endpoint para aceptar/rechazar solicitudes de eliminacion
    @PutMapping("/solicitudes/{id}")
    public SolicitudEliminacion modificarSolicitud(@PathVariable Integer id, @RequestBody Estado_Solicitud nuevoEstado) {
        return agregadorService.modificarEstadoSolicitud(id, nuevoEstado);

    }


    @GetMapping("/hechos")
    public List<Hecho> obtenerTodosLosHechos() {
        return this.agregadorService.obtenerTodosLosHechos();
    }

    @GetMapping("/search")
    public List<HechoSearchDTO> busquedaTextoLibre(@RequestParam(required = false) String texto) {
        return agregadorService.buscarTextoLibre(texto);
    }

    /* Ubicaciones para mapa front */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/hechos/ubicaciones")
    public List<UbicacionParaMapaDTO> obtenerUbicaciones() {
        return this.agregadorService.obtenerUbicaciones();
    }

}

