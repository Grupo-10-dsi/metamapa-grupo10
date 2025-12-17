package ar.edu.utn.frba.ddsi.dinamica.controllers;


import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.ContribuyenteDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.dinamica.services.DinamicaService;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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


    @PostMapping("/hechos")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Integer subirHecho(
            @RequestBody HechoDTO hechoDTO){
//            @AuthenticationPrincipal Jwt jwt) {
//
//        String userUuid = jwt.getSubject();
//        String userEmail = jwt.getClaimAsString("email");
//        String userName = jwt.getClaimAsString("name");
//
//        ContribuyenteDTO c = new ContribuyenteDTO(userUuid, userName, userEmail);
//        hechoDTO.setContribuyente(c);

        verificarCamposHecho(hechoDTO);
        return dinamicaService.crearHecho(hechoDTO);

    }

    @PutMapping("/hechos/{id}")
    public Hecho modificarHecho(@PathVariable Integer id, @RequestBody HechoDTO hechoDTO) {

        verificarCamposHecho(hechoDTO);

        return dinamicaService.actualizarHecho(id, hechoDTO);
    }


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
