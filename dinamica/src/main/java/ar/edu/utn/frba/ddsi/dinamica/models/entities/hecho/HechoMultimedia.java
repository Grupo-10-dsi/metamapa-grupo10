package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class HechoMultimedia extends Hecho {
    private List<String> contenidoMultimedia;

    public HechoMultimedia(String titulo,
                           String descripcion,
                           Categoria categoria,
                           Ubicacion ubicacion,
                           LocalDateTime fechaAcontecimiento,
                           List<Etiqueta> etiquetas,
                           Contribuyente contribuyente,
                           List<String> contenidoMultimedia) {

        super(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, etiquetas, contribuyente);

        this.contenidoMultimedia = contenidoMultimedia;
    }
}
