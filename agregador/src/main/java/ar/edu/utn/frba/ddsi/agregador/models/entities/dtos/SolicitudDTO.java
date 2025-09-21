package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SolicitudDTO {
    private Integer idHecho;
    private String justificacion;

    public SolicitudDTO(Integer id, String justificacion) {
        this.idHecho = id;
        this.justificacion = justificacion;
    }
}
