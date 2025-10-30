package ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

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
