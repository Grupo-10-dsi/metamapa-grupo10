package ar.edu.utn.frba.ddsi.agregador.controllers;


import ar.edu.utn.frba.ddsi.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.services.AgregadorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/agregador")
@RestController

public class AgregadorController {
    private final AgregadorService agregadorService;

    public AgregadorController(AgregadorService agregadorService) {
        this.agregadorService = agregadorService;
    }

    /**
     * Permite obtener una lista de hechos filtrados por los parametros. Los parametros se pasa
     * como param en una request GET y son opcionales. Si no se pasa ningun parametro,
     * se devuelven todos los hechos.
     * get ("http://localhost:8080/agregador/hechos?categoria=robo&fecha_reporte_desde=2023-01-08")""
     **/
    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fecha_reporte_desde,
            @RequestParam(required = false) String fecha_reporte_hasta,
            @RequestParam(required = false) String fecha_acontecimiento_desde,
            @RequestParam(required = false) String fecha_acontecimiento_hasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud,
            @RequestParam(required = false) Double coleccionId
    ) {
        return agregadorService.encontrarHechosFiltrados(
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
    public UUID subirHecho(@RequestBody HechoDTO hechoDTO) {

        verificarCamposHecho(hechoDTO);
        return dinamicaService.crearHecho(hechoDTO);

    }

    /**
     * Permite la modificacion de hecho pasando como cuerpo un hecho
     * con todos los campos requeridos (salvo contribuyente, que si es null
     * se asume como anonimo). Devuelve el nuevo hecho con la actualizacion.
     **/
    @PutMapping("/hechos/{id}")
    public Hecho modificarHecho(@PathVariable UUID id, @RequestBody HechoDTO hechoDTO) {

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
    public UUID subirSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        return dinamicaService.crearSolicitudEliminacion(solicitudDTO);
    }

    @PutMapping("/solicitudes/{id}")
    public SolicitudEliminacion modificarSolicitud(@PathVariable UUID id, @RequestBody Estado_Solicitud nuevoEstado) {
        return dinamicaService.modificarEstadoSolicitud(id, nuevoEstado);
    }
}
