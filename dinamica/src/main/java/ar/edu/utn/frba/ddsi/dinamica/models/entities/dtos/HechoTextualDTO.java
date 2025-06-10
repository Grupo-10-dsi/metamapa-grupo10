package ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HechoTextualDTO extends HechoDTO {
    private String cuerpo;  // Específico para hecho textual
}
