package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import lombok.Getter;

@Getter

public class SolicitudDTOE {
    private Integer id;
    private Integer idHecho;
    private String justificacion;
    private Estado_Solicitud estado;

    public SolicitudDTOE(Integer id, Integer idHecho, String justificacion, Estado_Solicitud estado) {
        this.id = id;
        this.idHecho = idHecho;
        this.justificacion = justificacion;
        this.estado = estado;
    }
}
