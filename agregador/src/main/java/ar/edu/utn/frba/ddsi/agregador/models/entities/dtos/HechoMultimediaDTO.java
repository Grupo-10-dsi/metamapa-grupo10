package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class HechoMultimediaDTO extends  HechoDTO {
    List<String> contenidoMultimedia;
}
