package ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = false)
public class HechoMultimediaDTO extends HechoDTO{
    private List<String> contenidoMultimedia;
}
