package ar.edu.utn.frba.ddsi.gestorpersonas.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private Integer contribuyente_id;
    private String contribuyente_nombre;

    public UsuarioDTO(Integer contribuyente_id, String contribuyente_nombre) {
        this.contribuyente_id = contribuyente_id;
        this.contribuyente_nombre = contribuyente_nombre;
    }
}




