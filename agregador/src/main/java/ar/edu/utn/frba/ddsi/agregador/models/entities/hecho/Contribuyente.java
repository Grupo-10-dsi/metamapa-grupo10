package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Contribuyente {
    private Integer contribuyente_id;
    private String contribuyente_nombre;

    public Contribuyente(Integer contribuyente_id, String contribuyente_nombre) {
        this.contribuyente_id = contribuyente_id;
        this.contribuyente_nombre = contribuyente_nombre;
    }

    public Contribuyente() {

    }
}
