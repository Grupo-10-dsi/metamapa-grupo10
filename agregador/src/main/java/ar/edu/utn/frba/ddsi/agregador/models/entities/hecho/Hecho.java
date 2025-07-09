package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;


import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public abstract class Hecho {
    private UUID id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaImportacion;
    private Origen_Fuente origenFuente;
    private List<Etiqueta> etiquetas;
    private Contribuyente contribuyente;
    private Integer cantidadMenciones;

    public Hecho(UUID id, String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, LocalDateTime fechaImportacion, Origen_Fuente origenFuente, List<Etiqueta> etiquetas, Contribuyente contribuyente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaImportacion = fechaImportacion;
        this.origenFuente = origenFuente;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
    }

    public boolean esEditable() {
        //return !this.esAnonimo && (ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7 );
        return ChronoUnit.DAYS.between(this.fechaImportacion, LocalDateTime.now()) < 7;
    }

    public boolean consensuado(Double mencionesNecesarias) {
        return this.cantidadMenciones >= mencionesNecesarias;
    }
}








