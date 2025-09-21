package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@DiscriminatorValue("TEXTUAL")
public class HechoTextual extends Hecho {
    private String cuerpo;
    
    public HechoTextual(String titulo,
                        String descripcion,
                        Categoria categoria,
                        Ubicacion ubicacion,
                        LocalDateTime fechaAcontecimiento,
                        List<Etiqueta> etiquetas,
                        Contribuyente contribuyente,
                        String cuerpo) {

        super(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, etiquetas, contribuyente);
        this.cuerpo = cuerpo;
    }

    public HechoTextual() {

    }
}
