package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubicacion {
    private Double latitud;
    private Double longitud;

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ubicacion() {}
}
