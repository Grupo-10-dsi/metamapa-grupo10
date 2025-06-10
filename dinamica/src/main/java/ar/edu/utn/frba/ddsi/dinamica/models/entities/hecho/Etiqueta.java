package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Etiqueta {
    private String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }

    public Etiqueta() {}
}
