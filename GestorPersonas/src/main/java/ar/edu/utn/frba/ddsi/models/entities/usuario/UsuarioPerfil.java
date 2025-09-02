package ar.edu.utn.frba.ddsi.models.entities.usuario;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UsuarioPerfil {

    private UUID id;
    private String nombre;
    private String email;
    private int edad;

    public UsuarioPerfil(UUID id, String nombre, String email, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
    }

}
