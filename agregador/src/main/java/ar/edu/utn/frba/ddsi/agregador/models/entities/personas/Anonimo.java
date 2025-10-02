package ar.edu.utn.frba.ddsi.agregador.models.entities.personas;

import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName("anonimo")
@Entity
@DiscriminatorValue("ANONIMO")
public class Anonimo extends Contribuyente {

    private static Anonimo instance;

    public Anonimo(Integer id) {
        // Constructor privado
        this.id = id;
    }

    public Anonimo() {

    }

    @JsonCreator
    public static Anonimo create() {
        return getInstance();
    }

//    @Override
//    public void asignarId() {
//        // Asignar manualmente ID -1 para anónimo
//        this.id = -1;
//    }

    public static Anonimo getInstance() {
        if (instance == null) {
            instance = new Anonimo();


        }
        return instance;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}