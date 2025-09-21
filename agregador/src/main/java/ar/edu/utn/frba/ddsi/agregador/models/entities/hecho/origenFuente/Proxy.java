package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

@Entity  @DiscriminatorValue("proxy")
public class Proxy extends OrigenFuente {

    public Proxy() {
        super();
    }
}
