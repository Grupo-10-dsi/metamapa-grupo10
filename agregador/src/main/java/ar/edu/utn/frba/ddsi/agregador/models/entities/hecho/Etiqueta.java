package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter

@Entity
public class Etiqueta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }

    public Etiqueta() {}
}
