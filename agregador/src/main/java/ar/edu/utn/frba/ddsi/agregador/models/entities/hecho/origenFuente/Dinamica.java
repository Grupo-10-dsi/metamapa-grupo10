package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import javax.persistence.*;

@Entity  @DiscriminatorValue("dinamica")
public class Dinamica extends OrigenFuente {

    public Dinamica() {
        super();
    }
}
