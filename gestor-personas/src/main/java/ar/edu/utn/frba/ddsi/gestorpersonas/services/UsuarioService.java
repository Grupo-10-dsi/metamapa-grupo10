package ar.edu.utn.frba.ddsi.gestorpersonas.services;

import ar.edu.utn.frba.ddsi.gestorpersonas.models.entities.KeycloakUserDTO;
import ar.edu.utn.frba.ddsi.gestorpersonas.models.entities.Usuario;
import ar.edu.utn.frba.ddsi.gestorpersonas.models.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario findOrCreateUsuario(KeycloakUserDTO requestDto) {
        // 1. Intenta buscar al usuario por su UUID de Keycloak
        return usuarioRepository.findByKeycloakSub(requestDto.getSub())
                .orElseGet(() -> {
                    // 2. Si no existe (orElseGet), lo crea
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setKeycloakSub(requestDto.getSub());
                    nuevoUsuario.setEmail(requestDto.getEmail());
                    nuevoUsuario.setNombre(requestDto.getNombre());

                    // 3. Guarda el nuevo usuario y lo retorna
                    return usuarioRepository.save(nuevoUsuario);
                });
    }
}
