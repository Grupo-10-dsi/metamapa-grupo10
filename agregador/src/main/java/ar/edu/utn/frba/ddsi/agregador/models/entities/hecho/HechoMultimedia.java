package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@Entity @DiscriminatorValue("MULTIMEDIA")
public class HechoMultimedia extends Hecho{

    @ElementCollection
    @CollectionTable(name = "hecho_multimedias", joinColumns = @JoinColumn(name = "hecho_id")) @Column(name = "contenido_multimedia")
    private List<String> contenidoMultimedia;

    public HechoMultimedia(Integer id,
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

    public HechoMultimedia() {

    }
}
