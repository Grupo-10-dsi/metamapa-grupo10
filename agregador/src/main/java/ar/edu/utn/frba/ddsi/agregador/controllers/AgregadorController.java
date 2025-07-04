package ar.edu.utn.frba.ddsi.agregador.controllers;


import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.services.AgregadorService;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/agregador")
@RestController

public class AgregadorController {
    private final AgregadorService agregadorService;

    public AgregadorController(AgregadorService agregadorService) {
        this.agregadorService = agregadorService;
    }

    /**
     * Crea una nueva coleccion en el sistema a partir del DTO que recibe.
     * Si se crea correctamente, devuelve el ID de la coleccion creada.
     */
    @PostMapping("/colecciones")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public UUID crearColeccion(@RequestBody ColeccionDTO coleccion) {
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

    /**
     * Devuelve una coleccion en particular a partir de su ID.
     * Si no se encuentra la coleccion, devuelve un error 404.
     */
    @GetMapping("/colecciones/{id}")
    public Coleccion obtenerColeccion(@PathVariable UUID id){
        return this.agregadorService.obtenerColeccion(id);
    }

    /**
     * Elimina una coleccion del sistema a partir de su ID. No devuelve contenido.
     * Si no se encuentra la coleccion, devuelve un error 404.
     */
    @DeleteMapping("/colecciones/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void eliminarColeccion(@PathVariable UUID id) {
        this.agregadorService.eliminarColeccionPorId(id);
    }

    /**
     * Permite modificar una coleccion.
     * Recibe el ID de la coleccion a modificar y un DTO con los nuevos datos.
     */
//    @PutMapping("/colecciones/{id}")
//    @ResponseStatus(org.springframework.http.HttpStatus.OK)
//    public Coleccion modificarColeccion(@PathVariable UUID id, @RequestBody ColeccionDTO coleccionDTO) {
//        return this.agregadorService.modificarColeccion(id, coleccionDTO);
//    }

    // Endpoint para aceptar/rechazar solicitudes de eliminacion

    @PutMapping("/solicitudes/{id}")
    public SolicitudEliminacion modificarSolicitud(@PathVariable UUID id, @RequestBody Estado_Solicitud nuevoEstado) {
        return agregadorService.modificarEstadoSolicitud(id, nuevoEstado);
    }

    // Endpoint para modificar algoritmo de consenso de una coleccion
    // @PatchMapping("/colecciones/{id}/{algoritmo}")
    //public void modificarAlgoritmoConsenso(@PathVariable UUID id, //@PathVariable algoritmo de que tipo seria??) {
    //   agregadorService.modificarAlgoritmoConsenso(id, algoritmo);
    //}









//@GetMapping("/solicitudes")
//public List<SolicitudEliminacion> obtenerSolicitudes() {// return agregadorService.encontrarSolicitudes();
}

