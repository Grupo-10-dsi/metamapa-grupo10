package ar.edu.utn.frba.ddsi.normalizador.models.entities.personas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class Registrado extends Contribuyente {

    // El id debe matchear con el id de la persona en el sistema de usuarios
    private String nombre;

    public Registrado(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
