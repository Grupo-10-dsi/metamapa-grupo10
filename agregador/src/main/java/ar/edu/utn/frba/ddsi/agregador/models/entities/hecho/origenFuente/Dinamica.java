package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

@Entity  @DiscriminatorValue("dinamica")
public class Dinamica extends OrigenFuente {

    public Dinamica() {
        super();
    }
}
