package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HechoTextual.class, name = "textual"),
        @JsonSubTypes.Type(value = HechoMultimedia.class, name = "multimedia")
})

public abstract class Hecho {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen_Fuente origen;
    private List<Etiqueta> etiquetas;
    private Contribuyente contribuyente;

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, List<Etiqueta> etiquetas, Contribuyente contribuyente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.origen = Origen_Fuente.INTERMEDIARIA;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
    }

    public boolean esEditable() {
        //return !this.esAnonimo && (ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7 );
        return ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7;
    }
}
