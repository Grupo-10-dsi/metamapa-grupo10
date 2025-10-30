package ar.edu.utn.frba.ddsi.gestorpersonas.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental (int/long)
    private Integer id;

    @Column(name = "keycloak_sub", unique = true, nullable = false, length = 36)
    private String keycloakSub; // El UUID de Keycloak

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nombre;


    public UsuarioDTO toDTO() {
        return new UsuarioDTO(this.id, this.nombre);
    }


}