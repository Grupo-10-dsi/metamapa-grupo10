package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;


@Getter
public class UbicacionParaMapaDTO {
    private Integer id;
    private String descripcion;
    private Double latitud;
    private Double longitud;

    public UbicacionParaMapaDTO(Integer id, String descripcion, Double latitud, Double longitud) {
        this.id = id;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}

