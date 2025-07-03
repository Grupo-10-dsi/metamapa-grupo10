package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Origen_Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class HechoDTO {
    private UUID id;
    private String tipo;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen_Fuente origenFuente;
    private List<Etiqueta> etiquetas;
    private List<String> contenidoMultimedia;
    private String cuerpo;
    private Contribuyente contribuyente;

    public HechoDTO(UUID id, String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, LocalDateTime fechaCarga, List<Etiqueta> etiquetas, List<String> contenidoMultimedia, String cuerpo, Origen_Fuente origenFuente, Contribuyente contribuyente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.etiquetas = etiquetas;
        this.contenidoMultimedia = contenidoMultimedia;
        this.cuerpo = cuerpo;
        this.origenFuente = origenFuente;
        this.contribuyente = contribuyente;

    }


}

