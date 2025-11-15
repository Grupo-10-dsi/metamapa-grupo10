package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Origen_Fuente_VIEJO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo" // Este será el "discriminador" en tu JSON
)
// 2. Mapeamos los valores de "tipo" a las clases DTO concretas
@JsonSubTypes({
        @JsonSubTypes.Type(value = HechoTextualDTO.class, name = "textual"),
        @JsonSubTypes.Type(value = HechoMultimediaDTO.class, name = "multimedia")
})
public class HechoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen_Fuente_VIEJO origenFuente;
    private List<Etiqueta> etiquetas;
    private ContribuyenteDTO contribuyente;

    public HechoDTO(Integer id, String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga, List<Etiqueta> etiquetas, Origen_Fuente_VIEJO origenFuente, ContribuyenteDTO contribuyente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.etiquetas = etiquetas;
        this.origenFuente = origenFuente;
        this.contribuyente = contribuyente;

    }




}

