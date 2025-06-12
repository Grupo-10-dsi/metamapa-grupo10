package ar.edu.utn.frba.ddsi.dinamica.controllers;


import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.dinamica.services.DinamicaService;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/dinamica")
@RestController

public class DinamicaController {
    private final DinamicaService dinamicaService;

    public DinamicaController(DinamicaService dinamicaService) {
        this.dinamicaService = dinamicaService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fecha_reporte_desde,
            @RequestParam(required = false) String fecha_reporte_hasta,
            @RequestParam(required = false) String fecha_acontecimiento_desde,
            @RequestParam(required = false) String fecha_acontecimiento_hasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud
    ) {
        return dinamicaService.encontrarHechosFiltrados(
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                latitud,
                longitud
        );
    }

    @PostMapping("/hechos")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public UUID subirHecho(@RequestBody HechoDTO hechoDTO) {

        verificarCamposHecho(hechoDTO);
        return dinamicaService.crearHecho(hechoDTO);

    }

    @PutMapping("/hechos/{id}")
    public void modificarHecho(@PathVariable UUID id, @RequestBody HechoDTO hechoDTO) {
        switch (hechoDTO.getTipo().toLowerCase()) {
            case "textual":
                dinamicaService.actualizarHechoTextual(id, hechoDTO);
                break;

            case "multimedia":
                dinamicaService.actualizarHechoMultimedia(id, hechoDTO);
                break;

            default:
                throw new IllegalArgumentException("Tipo de hecho no soportado: " + hechoDTO.getTipo());
        }
    }


    public void verificarCamposHecho(HechoDTO hechoDTO) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            Map<String, Object> hechoMap = mapper.convertValue(hechoDTO, Map.class);

        String[] camposObligatorios = {"titulo", "descripcion", "categoria", "ubicacion",
                "fechaAcontecimiento", "etiquetas"};

        for (String campo : camposObligatorios) {
            if (hechoMap.get(campo) == null) {
                throw new IllegalArgumentException("Campo obligatorio faltante: " + campo);
            }
        }

        switch (hechoDTO.getTipo().toLowerCase()) {
            case "textual":
                if( hechoDTO.getCuerpo() == null) {
                    throw new IllegalArgumentException("El campo 'cuerpo' es obligatorio para hechos de tipo 'textual'");
                }
                break;

            case "multimedia":
                if( hechoDTO.getContenidoMultimedia() == null) {
                    throw new IllegalArgumentException("El campo 'contenidoMultimedia' es obligatorio para hechos de tipo 'multimedia'");
                }
                break;

            default:
                throw new IllegalArgumentException("Tipo de hecho no soportado: " + hechoDTO.getTipo());
        }

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @GetMapping("/solicitudes")
    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return dinamicaService.encontrarSolicitudes();
    }

    @PostMapping("/solicitudes")
    public UUID subirSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        return dinamicaService.crearSolicitudEliminacion(solicitudDTO);
    }

    @PutMapping("/solicitudes/{id}")
    public SolicitudEliminacion modificarSolicitud(@PathVariable UUID id, @RequestBody Estado_Solicitud nuevoEstado) {
        return dinamicaService.modificarEstadoSolicitud(id, nuevoEstado);
    }
}
