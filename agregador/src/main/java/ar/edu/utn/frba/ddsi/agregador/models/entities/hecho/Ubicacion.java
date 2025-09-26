package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;


@Getter
@Setter



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
