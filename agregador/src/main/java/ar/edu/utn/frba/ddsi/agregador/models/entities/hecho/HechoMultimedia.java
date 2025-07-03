package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
                        LocalDate fechaAcontecimiento,
                        LocalDateTime fechaCarga,
                        Origen_Fuente origenFuente,
                        List<Etiqueta> etiquetas,
                        Contribuyente contribuyente,
                        List<String> contenidoMultimedia) {

        super(id, titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, fechaCarga, origenFuente, etiquetas, contribuyente);
        this.contenidoMultimedia = contenidoMultimedia;
    }
}
