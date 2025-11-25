package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.EtiquetaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Etiqueta;
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

    EtiquetaDTO map(Etiqueta value);

    default Origen_Fuente_VIEJO mapOrigenFuente(OrigenFuente origen) {
        if (origen == null) return null;
        if (origen instanceof Estatica) {
            return Origen_Fuente_VIEJO.ESTATICA;
        }
        String simple = origen.getClass().getSimpleName().toUpperCase();
        try {
            return Origen_Fuente_VIEJO.valueOf(simple);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
