package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ColeccionMapper {

    @Mapping(target = "urls_fuente", expression = "java(mapFuentesToUrls(coleccion.getFuentes()))")
    @Mapping(target = "criterios", source = "criterios")
    ColeccionDTO toColeccionDTO(Coleccion coleccion);

    default List<String> mapFuentesToUrls(List<Fuente> fuentes) {
        if (fuentes == null) {
            return new ArrayList<>();
        }
        return fuentes.stream()
                .map(Fuente::getUrl)
                .collect(Collectors.toList());
    }

    // Otros métodos del mapper...
}
