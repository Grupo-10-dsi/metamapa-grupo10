package ar.edu.utn.frba.ddsi.agregador.controllers;


import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.UbicacionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.services.EstadisticasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agregador/estadisticas")
public class EstadisticasController {
    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    //APIs que va a consumir el módulo de estadísticas, el agregador le brinda los datos y la estadistica los procesa.

    @GetMapping("/coleccion/{Id}/ubicaciones")
    public List<UbicacionDTO> obtenerUbicacionesColeccion(@PathVariable Integer Id) {
        List<Ubicacion> ubicaciones = this.estadisticasService.obtenerUbicacionesColeccion(Id);
        return ubicaciones.stream()
                .map(Ubicacion::toDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @GetMapping("/hechos/max-categoria")
    public Categoria obtenerCategoriaConMasHechos() {
        return this.estadisticasService.obtenerCategoriaConMasHechos();
    }

//    @GetMapping("/colecciones/{Id}/ubicacionMasFrecuente")
//    public UbicacionDTO obtenerUbicacionMasFrecuenteDeColeccion(@PathVariable Integer Id) {
//        Ubicacion ubicacion = this.estadisticasService.obtenerUbicacionMasFrecuenteDeColeccion(Id);
//        return ubicacion.toDTO();
//    }

    @GetMapping("/categoria/{Id}/ubicaciones")
    public List<UbicacionDTO> obtenerUbicacionesCategoria(@PathVariable Integer Id) {
        List<Ubicacion> ubicaciones = this.estadisticasService.obtenerUbicacionesCategoria(Id);
        return ubicaciones.stream()
                .map(Ubicacion::toDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @GetMapping("/categoria/{Id}/hora")
    public LocalTime obtenerHoraMasFrecuente(@PathVariable Integer Id) {
        return this.estadisticasService.obtenerHoraMasFrecuente(Id);
    }

    @GetMapping("/solicitudes/spam")
    public List<SolicitudDTO> obtenerSolicitudesSpam() {
        return this.estadisticasService.obtenerSolicitudesSpam();
    }
}
