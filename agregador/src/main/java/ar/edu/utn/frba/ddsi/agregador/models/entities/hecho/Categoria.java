package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter

@Embeddable
public class Categoria {


    @Column(name = "detalle", nullable = false, unique = true)
    private String detalle;

    public Categoria(String detalle) {
        this.detalle = detalle;
    }

    public Categoria() {}
}
