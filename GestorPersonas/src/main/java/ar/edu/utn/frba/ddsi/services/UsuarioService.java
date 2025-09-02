package ar.edu.utn.frba.ddsi.services;

import ar.edu.utn.frba.ddsi.models.entities.dtos.ContribuyenteDTO;
import ar.edu.utn.frba.ddsi.models.entities.dtos.UsuarioDTO;
import ar.edu.utn.frba.ddsi.models.entities.usuario.UsuarioPerfil;
import ar.edu.utn.frba.ddsi.models.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public int crearUsuario(UsuarioDTO user) {
        // verificar existencia
        // gestionar contraseña
        // crear
        // logear
        return 1;
    }

    public String login(String email, String password) {
        SessionManager sessionManager = SessionManager.get();
        UsuarioPerfil perfil = usuarioRepository.getPerfil(email, password);

        if (perfil == null) {
            return null;
        }

        return sessionManager.crearSesion("perfil", perfil);
    }

    public UsuarioPerfil perfil(String token) {
        SessionManager sessionManager = SessionManager.get();
        return (UsuarioPerfil) sessionManager.getPerfil(token);
    }

    /*
    * Para el caso que otro componente necesite un contribuyente para un hecho
    * */
    public ContribuyenteDTO getContribuyente(String token) {
        SessionManager sessionManager = SessionManager.get();
        UsuarioPerfil perfil = (UsuarioPerfil) sessionManager.getPerfil(token);
        if (perfil == null) {
            return null;
        }
        return this.toContribuyenteDTO(perfil);
    }

    private ContribuyenteDTO toContribuyenteDTO(UsuarioPerfil perfil) {
        return new ContribuyenteDTO(perfil.getId(), perfil.getNombre());
    }
}
