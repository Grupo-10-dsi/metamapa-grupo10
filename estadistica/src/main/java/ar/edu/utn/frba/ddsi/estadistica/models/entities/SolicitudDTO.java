package ar.edu.utn.frba.ddsi.estadistica.models.entities;


import lombok.Getter;

@Getter
public class SolicitudDTO {
    private Integer id;
    private Integer idHecho;
    private String justificacion;
    private Estado_Solicitud estadoSolicitud;

    public SolicitudDTO(Integer id, Integer idHecho,String justificacion, Estado_Solicitud estadoSolicitud) {
        this.id = id;
        this.idHecho = idHecho;
        this.justificacion = justificacion;
        this.estadoSolicitud = estadoSolicitud;
    }
}
