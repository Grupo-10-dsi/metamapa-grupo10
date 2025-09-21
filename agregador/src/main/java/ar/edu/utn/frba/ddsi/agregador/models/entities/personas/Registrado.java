package ar.edu.utn.frba.ddsi.agregador.models.entities.personas;

import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class Registrado extends Contribuyente {

    // El id debe matchear con el id de la persona en el sistema de usuarios
    private Integer id;
    private String nombre;

    public Registrado(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
