package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.*;
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
    @Mapping(target = "origenFuente", expression = "java(mapOrigenFuenteDTO(hecho.getOrigenFuente()))")
    HechoDTO toHechoDTO(Hecho hecho);

    @Mapping(target = "origenFuente", expression = "java(mapOrigenFuenteDTO(hecho.getOrigenFuente()))")
    HechoTextualDTO toDTO(HechoTextual hecho);

    @Mapping(target = "origenFuente", expression = "java(mapOrigenFuenteDTO(hecho.getOrigenFuente()))")
    HechoMultimediaDTO toDTO(HechoMultimedia hecho);

    EtiquetaDTO map(Etiqueta value);

    default OrigenFuenteDTO mapOrigenFuenteDTO(OrigenFuente origen) {
        if (origen == null) {
            return null;
        }

        String tipo = origen.getClass().getSimpleName().toUpperCase();
        String nombreArchivo = null;

        if (origen instanceof Estatica) {
            Estatica estatica = (Estatica) origen;
            nombreArchivo = estatica.getArchivoProcesado() != null
                    ? estatica.getArchivoProcesado().getNombre()
                    : null;
        }

        return new OrigenFuenteDTO(tipo, origen.getNombre(), nombreArchivo);
    }
}

