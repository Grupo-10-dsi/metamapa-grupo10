package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;

/* Tener una estructrua basica de hecho para listarlo como resultado */
@Getter
@Setter
public class HechoSearchDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Double relevancia;

    public HechoSearchDTO(Integer id, String titulo, String descripcion, Double relevancia) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.relevancia = relevancia;
    }
}
