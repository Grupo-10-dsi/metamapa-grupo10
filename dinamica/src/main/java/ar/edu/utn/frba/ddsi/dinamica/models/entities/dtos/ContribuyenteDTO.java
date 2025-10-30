package ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContribuyenteDTO {
    private String sub;
    private String nombre;
    private String email;
    public ContribuyenteDTO(String sub, String nombre, String email ) {
        this.sub = sub;
        this.nombre = nombre;
        this.email = email;
    }
}
