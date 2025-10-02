package ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion;

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

    private Integer idHecho;
    private String justificacion;

    @Enumerated(EnumType.STRING)
    private Estado_Solicitud estado;


    public SolicitudEliminacion(Integer idHecho, String justificacion) {
        this.idHecho = idHecho;
        this.justificacion = justificacion;
        this.estado = Estado_Solicitud.PENDIENTE;
    }

    public SolicitudEliminacion() {}



    public boolean esCorrecta() {
        // Justificacion con minimo 500 caracteres
        return justificacion != null && justificacion.length() >= 500;
    }

}
