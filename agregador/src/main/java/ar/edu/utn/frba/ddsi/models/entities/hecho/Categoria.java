package ar.edu.utn.frba.ddsi.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Categoria {
    private String detalle;

    public Categoria(String detalle) {
        this.detalle = detalle;
    }

    public Categoria() {}
}
