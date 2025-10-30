package ar.edu.utn.frba.ddsi.gestorpersonas.models.repositories;

import ar.edu.utn.frba.ddsi.gestorpersonas.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su ID de Keycloak (el UUID 'sub').
     * Spring Data JPA crea la consulta automáticamente.
     */
    Optional<Usuario> findByKeycloakSub(String keycloakSub);
}