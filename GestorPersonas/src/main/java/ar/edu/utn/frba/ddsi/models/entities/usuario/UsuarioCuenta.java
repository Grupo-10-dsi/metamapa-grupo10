package ar.edu.utn.frba.ddsi.models.entities.usuario;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UsuarioCuenta {

    private UUID id;
    private String username;
    private String password;
    private UsuarioPerfil perfil;

    public UsuarioCuenta(UUID id, String password, UsuarioPerfil perfil) {
        this.id = id;
        this.password = password;
        this.perfil = perfil;
    }


}
