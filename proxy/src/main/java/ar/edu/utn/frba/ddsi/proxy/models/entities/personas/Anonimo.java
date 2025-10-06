package ar.edu.utn.frba.ddsi.proxy.models.entities.personas;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName("anonimo")
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


    public static Anonimo getInstance() {
        if (instance == null) {
            instance = new Anonimo();
        }
        return instance;
    }

    @Override
    public String getNombre() {
        return "Anonimo";
    }

    public void setId(Integer id) {
        this.id = id;
    }

}