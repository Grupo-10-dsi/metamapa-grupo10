package ar.edu.utn.frba.ddsi.gestorpersonas.controllers;

import ar.edu.utn.frba.ddsi.gestorpersonas.models.entities.KeycloakUserDTO;
import ar.edu.utn.frba.ddsi.gestorpersonas.models.entities.Usuario;
import ar.edu.utn.frba.ddsi.gestorpersonas.models.entities.UsuarioDTO;
import ar.edu.utn.frba.ddsi.gestorpersonas.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/usuarios")
public class GestorPersonasController {
    private final UsuarioService usuarioService;

    @Autowired
    public GestorPersonasController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/find-or-create")
    public UsuarioDTO findOrCreate(@RequestBody KeycloakUserDTO keycloakUser) {
        Usuario usuario = usuarioService.findOrCreateUsuario(keycloakUser);
        return usuario.toDTO(); // devuelve un DTO con el id
    }
}