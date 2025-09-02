package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class HechoMultimedia extends Hecho{
    private List<String> contenidoMultimedia;

    public HechoMultimedia(UUID id,
                        String titulo,
                        String descripcion,
                        Categoria categoria,
                        Ubicacion ubicacion,
                        LocalDateTime fechaAcontecimiento,
                        LocalDateTime fechaCarga,
                        OrigenFuente origenFuente,
                        List<Etiqueta> etiquetas,
                        Contribuyente contribuyente,
                        List<String> contenidoMultimedia) {

        super(id, titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, fechaCarga, origenFuente, etiquetas, contribuyente);
        this.contenidoMultimedia = contenidoMultimedia;
    }
}
