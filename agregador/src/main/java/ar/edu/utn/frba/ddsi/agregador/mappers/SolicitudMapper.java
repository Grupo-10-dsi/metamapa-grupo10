package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTOE;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {
    @Mapping(target = "idHecho", source = "hecho.id")
    SolicitudDTOE toSolicitudDTOE(SolicitudEliminacion solicitudEliminacion);
}
