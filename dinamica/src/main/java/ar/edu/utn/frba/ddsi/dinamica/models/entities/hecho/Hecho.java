package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;


@Getter
@Setter



@Entity @Table(name="Hecho")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class Hecho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="categoria_id",referencedColumnName = "id")
    private Categoria categoria;

    @Embedded
    private Ubicacion ubicacion;

    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;

    @Enumerated(EnumType.STRING)
    private Origen_Fuente origenFuente;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id",referencedColumnName = "id")
    private List<Etiqueta> etiquetas;

//    private Integer contribuyente_id;
//    private String nombre_contribuyente;


//    @ManyToOne
//    @JoinColumn(name="contribuyente_id",referencedColumnName = "id")
    @Embedded
    private Contribuyente contribuyente;


    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, List<Etiqueta> etiquetas, Contribuyente contribuyente) {
       // this.id = Integer;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.origenFuente = Origen_Fuente.DINAMICA;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
    }

    public boolean esEditable() {
        //return !this.esAnonimo && (ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7 );
        return ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < 7;
    }

    public Hecho(){}
}








