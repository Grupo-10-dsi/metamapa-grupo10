package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;


@Getter
public class UbicacionParaMapaDTO {
    private Integer id;
    private Double latitud;
    private Double longitud;

    public UbicacionParaMapaDTO(Integer id, Double latitud, Double longitud) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}

