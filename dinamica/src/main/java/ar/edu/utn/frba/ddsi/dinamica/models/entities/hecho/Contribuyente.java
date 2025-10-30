package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
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
