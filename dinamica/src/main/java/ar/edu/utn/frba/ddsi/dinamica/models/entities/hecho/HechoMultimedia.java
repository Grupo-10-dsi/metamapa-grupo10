package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity @DiscriminatorValue("MULTIMEDIA")
public class HechoMultimedia extends Hecho {


    @ElementCollection
    @CollectionTable(name = "hecho_multimedias", joinColumns = @JoinColumn(name = "hecho_id")) @Column(name = "contenido_multimedia")
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

    public HechoMultimedia() {

    }
}
