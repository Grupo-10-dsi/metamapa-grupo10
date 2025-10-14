package ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name="Solicitud_Eliminacion")
public class SolicitudEliminacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="hecho_id",referencedColumnName = "id")
    private Hecho hecho;

    private String justificacion;

    @Enumerated(EnumType.STRING)
    private Estado_Solicitud estado;


    public SolicitudEliminacion() {
        this.estado = Estado_Solicitud.PENDIENTE; // Estado inicial por defecto
    }

    public boolean esCorrecta() {
        // Justificacion con minimo 500 caracteres
        return justificacion != null && justificacion.length() >= 500;
    }

}
