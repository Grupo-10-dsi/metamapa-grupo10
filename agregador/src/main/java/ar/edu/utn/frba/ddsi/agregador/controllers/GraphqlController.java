package ar.edu.utn.frba.ddsi.agregador.controllers;

import ar.edu.utn.frba.ddsi.agregador.mappers.HechoMapper;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
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

    public GraphqlController(AgregadorService agregadorService, HechoMapper hechoMapper) {
        this.agregadorService = agregadorService;
        this.hechoMapper = hechoMapper;
    }

    @QueryMapping
    public List<HechoDTO> hechos() {
        return agregadorService.obtenerTodosLosHechos().stream().map(hechoMapper::toHechoDTO).collect(Collectors.toList());
    }

    @QueryMapping
    public HechoDTO hecho(@Argument Integer id) {
        return hechoMapper.toHechoDTO(agregadorService.obtenerHechoPorId(id));
    }
}
