package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import jakarta.persistence.*;

@Entity  @DiscriminatorValue("proxy")
public class Proxy extends OrigenFuente {

    public Proxy() {
        super();
        this.nombre = "Proxy";

    }
}
