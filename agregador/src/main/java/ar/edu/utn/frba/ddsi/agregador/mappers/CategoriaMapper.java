package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CategoriaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaDTO toCategoriaDTO(Categoria categoria);
}
