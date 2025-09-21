package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter

@Embeddable
public class Categoria {
    @Column
    private String detalle;

    public Categoria(String detalle) {
        this.detalle = detalle;
    }

    public Categoria(){}
}
