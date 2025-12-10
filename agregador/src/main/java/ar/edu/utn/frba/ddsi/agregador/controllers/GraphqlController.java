package ar.edu.utn.frba.ddsi.agregador.controllers;

import ar.edu.utn.frba.ddsi.agregador.mappers.ColeccionMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.HechoMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.SolicitudMapper;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.agregador.services.AgregadorService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GraphqlController {

    private final AgregadorService agregadorService;
    private final HechoMapper hechoMapper;
    private final ColeccionMapper coleccionMapper;
    private final SolicitudMapper solicitudMapper;

    public GraphqlController(AgregadorService agregadorService, HechoMapper hechoMapper,
                            ColeccionMapper coleccionMapper, SolicitudMapper solicitudMapper) {
        this.agregadorService = agregadorService;
        this.hechoMapper = hechoMapper;
        this.coleccionMapper = coleccionMapper;
        this.solicitudMapper = solicitudMapper;
    }

    @QueryMapping
    public List<HechoDTOGraph> hechos() {
        return agregadorService.obtenerTodosLosHechos().stream().map(hechoMapper::toHechoDTO).collect(Collectors.toList());
    }

    @QueryMapping
    public HechoDTOGraph hecho(@Argument Integer id) {
        return hechoMapper.toHechoDTO(agregadorService.obtenerHechoPorId(id));
    }

    @QueryMapping
    public List<HechoDTOGraph> hechosPorContribuyente(@Argument Integer contribuyenteId) {
        return agregadorService.obtenerHechosPorContribuyente(contribuyenteId).stream()
                .map(hechoMapper::toHechoDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<ColeccionDTO> colecciones() {
        return agregadorService.obtenerColecciones().stream()
                .map(coleccionMapper::toColeccionDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public ColeccionDTO coleccion(@Argument Integer id) {
        return coleccionMapper.toColeccionDTO(agregadorService.obtenerColeccion(id));
    }

    @QueryMapping
    public List<SolicitudDTOE> solicitudes() {
        return agregadorService.encontrarSolicitudes().stream()
                .map(solicitudMapper::toSolicitudDTOE)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<SolicitudDTOE> solicitudesPendientes() {
        return agregadorService.encontrarSolicitudesPendientes().stream()
                .map(solicitudMapper::toSolicitudDTOE)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public SolicitudDTOE solicitud(@Argument Integer id) {
        return solicitudMapper.toSolicitudDTOE(
            agregadorService.encontrarSolicitudes().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null)
        );
    }

    @QueryMapping
    public ContribuyenteDTO contribuyente(@Argument Integer id) {
        Contribuyente contribuyente = agregadorService.obtenerContribuyente(id);
        if (contribuyente == null) {
            return null;
        }
        return new ContribuyenteDTO(contribuyente.getId(), contribuyente.getNombre());
    }

    @QueryMapping
    public List<HechoDTOGraph> hechosPorEtiquetas(@Argument List<String> nombres, @Argument(name = "match") String match) {
        boolean matchAll = "ALL".equalsIgnoreCase(match);
        return agregadorService.obtenerHechosPorEtiquetas(nombres, matchAll)
                .stream()
                .map(hechoMapper::toHechoDTO)
                .collect(Collectors.toList());
    }
}
