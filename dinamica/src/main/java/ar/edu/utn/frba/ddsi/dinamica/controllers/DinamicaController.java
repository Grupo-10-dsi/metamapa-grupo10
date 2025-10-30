package ar.edu.utn.frba.ddsi.dinamica.controllers;


import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.dinamica.services.DinamicaService;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Permite obtener una lista de hechos filtrados por los parametros. Los parametros se pasa
     * como param en una request GET y son opcionales. Si no se pasa ningun parametro,
     * se devuelven todos los hechos.
     **/
    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(
            @RequestParam(required = false) String ultimaConsulta,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fecha_reporte_desde,
            @RequestParam(required = false) String fecha_reporte_hasta,
            @RequestParam(required = false) String fecha_acontecimiento_desde,
            @RequestParam(required = false) String fecha_acontecimiento_hasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud
    ) {
        return dinamicaService.encontrarHechosFiltrados(
                ultimaConsulta,
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                latitud,
                longitud
        );
    }

    /**
    * Permite la creacion de hecho pasando como cuerpo un hecho
    * con todos los campos requeridos (salvo contribuyente, que si es null
     * se asume como anonimo). Devuelve el ID del hecho creado.
    **/
    @PostMapping("/hechos")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Integer subirHecho(@RequestBody HechoDTO hechoDTO) {

        verificarCamposHecho(hechoDTO);
        return dinamicaService.crearHecho(hechoDTO);

    }

    /**
     * Permite la modificacion de hecho pasando como cuerpo un hecho
     * con todos los campos requeridos (salvo contribuyente, que si es null
     * se asume como anonimo). Devuelve el nuevo hecho con la actualizacion.
     **/
    @PutMapping("/hechos/{id}")
    public Hecho modificarHecho(@PathVariable Integer id, @RequestBody HechoDTO hechoDTO) {

        verificarCamposHecho(hechoDTO);

        return dinamicaService.actualizarHecho(id, hechoDTO);
    }

    /**
     * Verifica que los campos obligatorios del HechoDTO esten presentes
     * y lanza una excepcion si alguno falta. Ademas, verifica los campos segun el tipo de hecho.
     **/
    private void verificarCamposHecho(HechoDTO hechoDTO) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Map<String, Object> hechoMap = mapper.convertValue(hechoDTO, Map.class);

        String[] camposObligatorios = {"tipo", "titulo", "descripcion", "categoria", "ubicacion",
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

    }

    @GetMapping("/solicitudes")
    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return dinamicaService.encontrarSolicitudes();
    }

    @PostMapping("/solicitudes")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Integer subirSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        return dinamicaService.crearSolicitudEliminacion(solicitudDTO);
    }

    @PutMapping("/solicitudes/{id}")
    public SolicitudEliminacion modificarSolicitud(@PathVariable Integer id, @RequestBody Estado_Solicitud nuevoEstado) {
        return dinamicaService.modificarEstadoSolicitud(id, nuevoEstado);
    }

    @PostMapping("/upload/{id}")
    public void cargarImagen(@PathVariable Integer id, @RequestParam MultipartFile file) {
        dinamicaService.guardarImagen(id, file);
    }

}
