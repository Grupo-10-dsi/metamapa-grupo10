package ar.edu.utn.frba.ddsi.agregador.models.entities.personas;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Registrado.class, name = "registrado"),
        @JsonSubTypes.Type(value = Anonimo.class, name = "anonimo")
})

@Getter
@Entity @Table(name="Contribuyente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class Contribuyente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @OneToMany(mappedBy = "contribuyente")
    private List<Hecho> hechos;

    // Por ahora la defino como abstracta y sin metodos. TODO ver que agregar
    public Contribuyente() {}

    public String getNombre() {
        return "";
    }

//    @PrePersist
//    public void asignarId() {
//        // Por defecto no hace nada, la base de datos asignará ID
//    }
}
