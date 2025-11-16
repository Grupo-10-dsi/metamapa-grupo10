package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Etiqueta;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EtiquetaDTO {
    private Integer id;
    private String descripcion;

    public EtiquetaDTO(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    // Optional alias if GraphQL schema still uses 'nombre'
    public String getNombre() {
        return descripcion;
    }

    public static EtiquetaDTO fromEntity(Etiqueta e) {
        return e == null ? null : new EtiquetaDTO(e.getId(), e.getDescripcion());
    }
}
