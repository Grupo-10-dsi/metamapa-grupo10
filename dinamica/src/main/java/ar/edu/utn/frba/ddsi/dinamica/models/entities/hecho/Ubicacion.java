package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.*;

@Getter
@Setter

@Embeddable
public class Ubicacion {
    @Column
    private Double latitud;
    @Column
    private Double longitud;

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ubicacion() {}
}
