package ar.edu.utn.frba.ddsi.gestorpersonas.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakUserDTO {
    private String nombre;
    private String sub;
    private String email;

    public KeycloakUserDTO(String nombre, String sub, String email) {
        this.nombre = nombre;
        this.sub = sub;
        this.email = email;
    }
}
