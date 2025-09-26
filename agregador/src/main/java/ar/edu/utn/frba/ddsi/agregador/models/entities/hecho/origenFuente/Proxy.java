package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import javax.persistence.*;

@Entity  @DiscriminatorValue("proxy")
public class Proxy extends OrigenFuente {

    public Proxy() {
        super();
    }
}
