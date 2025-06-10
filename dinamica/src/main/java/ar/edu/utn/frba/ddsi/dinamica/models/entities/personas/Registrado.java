package ar.edu.utn.frba.ddsi.dinamica.models.entities.personas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class Registrado extends Contribuyente {

    private UUID id;
    private String nombre;
    private String email;
    private int edad;

    public Registrado(String nombre, String email, int edad) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
    }


}
