package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;


import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter

@Entity @Table(name="Hecho")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descripcion;
    private Categoria categoria;

    @Embedded
    private Ubicacion ubicacion;

    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "origen_fuente_id")
    private OrigenFuente origenFuente;

    @OneToMany
    @JoinColumn(name = "hecho_id",referencedColumnName = "id")
    private List<Etiqueta> etiquetas;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contribuyente_id", referencedColumnName = "id")
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

    public Hecho() {

    }


    public boolean esEditable() {
        //return !this.esAnonimo && (ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7 );
        return ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7;
    }



    public boolean consensuado(Double mencionesNecesarias) {
        return this.cantidadMenciones >= mencionesNecesarias;
    }
}








