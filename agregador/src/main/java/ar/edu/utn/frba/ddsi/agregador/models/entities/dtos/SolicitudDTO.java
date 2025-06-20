package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudDTO {
    private UUID idHecho;
    private String justificacion;

    public SolicitudDTO(UUID id, String justificacion) {
        this.idHecho = id;
        this.justificacion = justificacion;
    }
}
