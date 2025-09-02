package ar.edu.utn.frba.ddsi.services;

import ar.edu.utn.frba.ddsi.models.entities.dtos.UsuarioDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UsuarioService {

    public int crearUsuario(@RequestBody(required = true) UsuarioDTO user) {
        // verificar existencia
        // gestionar contraseña
        // crear
        // logear
        return 1;
    }

}
