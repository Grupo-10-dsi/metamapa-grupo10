package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List; // <-- IMPORTANTE
import java.util.stream.Collectors; // <-- IMPORTANTE

@Mapper(componentModel = "spring")
public interface ColeccionMapper {

    @Mapping(source = "fuentes", target = "urls_fuente", qualifiedByName = "urls_fuenteToStrings")
    ColeccionDTO toColeccionDTO(Coleccion coleccion);

    @Named("urls_fuenteToStrings") // Le damos un nombre para usarlo en @Mapping
    default List<String> mapFuentesToStrings(List<Fuente> fuentes) {
        if (fuentes == null) {
            return null;
        }
        return fuentes.stream()
                .map(Fuente::getUrl)
                .collect(Collectors.toList());
    }
}
