package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Origen_Fuente_VIEJO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.Estatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface HechoMapper {

    @SubclassMapping(source = HechoTextual.class, target = HechoTextualDTO.class)
    @SubclassMapping(source = HechoMultimedia.class, target = HechoMultimediaDTO.class)
    @Mapping(target = "origenFuente", expression = "java(mapOrigenFuente(hecho.getOrigenFuente()))")
    HechoDTO toHechoDTO(Hecho hecho);

    @Mapping(target = "origenFuente", expression = "java(mapOrigenFuente(hecho.getOrigenFuente()))")
    HechoTextualDTO toDTO(HechoTextual hecho);


    @Mapping(target = "origenFuente", expression = "java(mapOrigenFuente(hecho.getOrigenFuente()))")
    HechoMultimediaDTO toDTO(HechoMultimedia hecho);

    // Helper: map entity's OrigenFuente (polymorphic) to DTO enum
    default Origen_Fuente_VIEJO mapOrigenFuente(OrigenFuente origen) {
        if (origen == null) return null;
        // Known concrete types
        if (origen instanceof Estatica) {
            return Origen_Fuente_VIEJO.ESTATICA;
        }
        // Avoid hard dependency on other concrete classes: infer by simple name
        String simple = origen.getClass().getSimpleName().toUpperCase();
        try {
            // If enum names match concrete type names, this will work
            return Origen_Fuente_VIEJO.valueOf(simple);
        } catch (IllegalArgumentException ex) {
            // Fallback if there is a different naming scheme: return null or choose a default
            return null;
        }
    }
}
