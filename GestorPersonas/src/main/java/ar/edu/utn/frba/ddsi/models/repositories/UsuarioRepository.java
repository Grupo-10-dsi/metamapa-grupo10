package ar.edu.utn.frba.ddsi.models.repositories;

import ar.edu.utn.frba.ddsi.models.entities.usuario.UsuarioCuenta;
import ar.edu.utn.frba.ddsi.models.entities.usuario.UsuarioPerfil;

import java.util.List;
import java.util.UUID;

public class UsuarioRepository {

    // PERSISTENCIA SIMULADA
    private UsuarioPerfil chandler = new UsuarioPerfil(
            java.util.UUID.randomUUID(),
            "Chandler Bing",
            "bing@hotmail.com",
            30
    );

    private List<UsuarioCuenta> cuentas = List.of(
            new UsuarioCuenta(UUID.randomUUID(), "1234", chandler)
    );

    public UsuarioPerfil getPerfil(String email, String password) {
        return cuentas.stream()
                .filter(c -> c.getPerfil().getEmail().equals(email) && c.getPassword().equals(password))
                .findFirst()
                .map(UsuarioCuenta::getPerfil)
                .orElse(null);

    }
}
