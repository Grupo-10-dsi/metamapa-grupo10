package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Origen_Fuente_VIEJO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class HechoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen_Fuente_VIEJO origenFuente;
    private List<EtiquetaDTO> etiquetas;
    private ContribuyenteDTO contribuyente;

    public List<String> getEtiquetasDescripciones() {
        return etiquetas == null ? List.of() :
                etiquetas.stream().map(EtiquetaDTO::getDescripcion).toList();
    }

    // Helper for manual construction if needed
    public void setEtiquetasFromEntities(List<Etiqueta> origen) {
        this.etiquetas = origen == null ? List.of() :
                origen.stream().map(EtiquetaDTO::fromEntity).collect(Collectors.toList());
    }
}
