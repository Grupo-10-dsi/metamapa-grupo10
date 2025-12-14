package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho;


import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // O "tipo", esto agrega un campo extra al JSON
)
@JsonSubTypes({
        // Aquí le dices: "Si es MULTIMEDIA, usa la clase HechoMultimedia y muestra sus campos"
        @JsonSubTypes.Type(value = HechoMultimedia.class, name = "MULTIMEDIA"),
        @JsonSubTypes.Type(value = HechoTextual.class, name = "TEXTUAL")
})
public abstract class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categoria categoria;

    @Embedded
    private Ubicacion ubicacion;

//    @ManyToOne
////    @JoinColumn(name = "url_fuente", referencedColumnName = "url") // la FK
////    @JsonIgnore
//    private Fuente fuente;

    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;

    @ManyToOne
    @JoinColumn(name = "origen_fuente_id", referencedColumnName = "id")
    private OrigenFuente origenFuente;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id",referencedColumnName = "id")
    private List<Etiqueta> etiquetas;

//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
//    @JoinColumn(name = "contribuyente_id", referencedColumnName = "id")
    @Embedded
    private Contribuyente contribuyente;



    private Integer cantidadMenciones;

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga, OrigenFuente origenFuente, List<Etiqueta> etiquetas, Contribuyente contribuyente) {
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








