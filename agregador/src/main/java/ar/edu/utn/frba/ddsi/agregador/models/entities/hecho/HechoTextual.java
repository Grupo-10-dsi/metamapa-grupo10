package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;



import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;


@Getter
@Setter
@Entity
@DiscriminatorValue("TEXTUAL")
public class HechoTextual extends Hecho {
    @Column(name = "Cuerpo")
    private String cuerpo;

    public HechoTextual(Integer id,
                        String titulo,
                        String descripcion,
                        Categoria categoria,
                        Ubicacion ubicacion,
                        LocalDateTime fechaAcontecimiento,
                        LocalDateTime fechaCarga,
                        OrigenFuente origenFuente,
                        List<Etiqueta> etiquetas,
                        Contribuyente contribuyente,
                        String cuerpo) {

        super(id, titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, fechaCarga, origenFuente, etiquetas, contribuyente);
        this.cuerpo = cuerpo;
    }
    public HechoTextual() {

    }
}