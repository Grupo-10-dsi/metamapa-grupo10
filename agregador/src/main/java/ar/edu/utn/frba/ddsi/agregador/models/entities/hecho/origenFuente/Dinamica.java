package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import jakarta.persistence.*;

@Entity  @DiscriminatorValue("dinamica")
public class Dinamica extends OrigenFuente {

    public Dinamica() {
        super();
        this.nombre = "DINAMICA";
    }
}
