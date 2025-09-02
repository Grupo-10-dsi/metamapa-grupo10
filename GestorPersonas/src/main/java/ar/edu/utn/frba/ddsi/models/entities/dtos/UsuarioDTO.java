package ar.edu.utn.frba.ddsi.models.entities.dtos;

import java.util.UUID;

public class UsuarioDTO {
    private UUID id;
    private String nombre;
    private String email;
    private int edad;

    public UsuarioDTO(UUID id, String nombre, String email, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
    }
}
