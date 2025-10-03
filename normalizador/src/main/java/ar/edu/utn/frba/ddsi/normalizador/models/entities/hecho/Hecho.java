package ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho;

import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.personas.Contribuyente;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Getter
@Setter



public abstract class Hecho {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private OrigenFuente origenFuente;
    private List<Etiqueta> etiquetas;
    private Contribuyente contribuyente;
    private Integer cantidadMenciones;

    public Hecho(Integer id, String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga, OrigenFuente origenFuente, List<Etiqueta> etiquetas, Contribuyente contribuyente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.origenFuente = origenFuente;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
    }

    public boolean esEditable() {
        //return !this.esAnonimo && (ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7 );
        return ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7;
    }

    public boolean consensuado(Double mencionesNecesarias) {
        return this.cantidadMenciones >= mencionesNecesarias;
    }
}








