package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContribuyenteDTO {
    private Integer contribuyente_id;
    private String contribuyente_nombre;

    public ContribuyenteDTO(Integer contribuyente_id, String contribuyente_nombre) {
        this.contribuyente_id = contribuyente_id;
        this.contribuyente_nombre = contribuyente_nombre;
    }

}
