package ar.edu.utn.frba.ddsi.controllers;

import ar.edu.utn.frba.ddsi.models.entities.dtos.UsuarioDTO;
import ar.edu.utn.frba.ddsi.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping
    public int crearUsuario(@RequestBody(required = true) UsuarioDTO user) {
        return usuarioService.crearUsuario(user);
    }

}
