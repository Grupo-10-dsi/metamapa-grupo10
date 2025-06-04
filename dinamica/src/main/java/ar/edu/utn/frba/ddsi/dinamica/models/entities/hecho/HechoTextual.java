package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoTextual extends Hecho {
    private String cuerpo;
    
    public HechoTextual(String titulo,
                        String descripcion,
                        Categoria categoria,
                        Ubicacion ubicacion,
                        LocalDateTime fechaAcontecimiento,
                        LocalDateTime fechaCarga,
                        List<Etiqueta> etiquetas,
                        Contribuyente contribuyente,
                        boolean esAnonimo,
                        String cuerpo) {

        super(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, fechaCarga, etiquetas, contribuyente, esAnonimo);
        this.cuerpo = cuerpo;
    }
}
