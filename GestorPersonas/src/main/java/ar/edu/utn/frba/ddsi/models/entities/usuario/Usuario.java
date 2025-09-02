package ar.edu.utn.frba.ddsi.models.entities.usuario;

import java.util.UUID;

public class Usuario {

    private UUID id;
    private String nombre;
    private String email;
    private int edad;

    public Usuario(UUID id, String nombre, String email, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
    }

}
