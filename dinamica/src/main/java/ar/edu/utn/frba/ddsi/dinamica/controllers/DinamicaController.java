package ar.edu.utn.frba.ddsi.dinamica.controllers;


import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.dinamica.services.DinamicaService;


import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.UUID;

@RequestMapping("/api/dinamica")
@RestController

public class DinamicaController {
    private final DinamicaService dinamicaService;

    public DinamicaController(DinamicaService dinamicaService) {
        this.dinamicaService = dinamicaService;
    }

    @PostMapping("/hechos")
    public Hecho subirHecho(@RequestBody Object hechoDTO){
        return dinamicaService.crearHecho(hechoDTO);
    }

    @PutMapping("/hechos/{id}")
    public void modificarHecho(@PathVariable UUID id, @RequestBody Object hechoDTO) {
        dinamicaService.actualizarHecho(id, hechoDTO);
    }


    @PostMapping("/solicitudes")
    public UUID subirSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        return dinamicaService.crearSolicitudEliminacion(solicitudDTO);
    }

    @PutMapping("/solicitudes/{id}")
    public void modificarSolicitud(@PathVariable UUID id, @RequestBody Estado_Solicitud nuevoEstado) {
        dinamicaService.modificarEstadoSolicitud(id, nuevoEstado);
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos() {
        return dinamicaService.encontrarHechos();
    }




}
