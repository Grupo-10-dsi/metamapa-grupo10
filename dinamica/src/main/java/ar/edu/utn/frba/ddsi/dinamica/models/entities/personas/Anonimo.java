package ar.edu.utn.frba.ddsi.dinamica.models.entities.personas;

import com.fasterxml.jackson.annotation.*;

import lombok.Getter;

import lombok.Setter;


@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName("anonimo")
public class Anonimo extends Contribuyente {

    private static Anonimo instance;

    private Anonimo() {
        // Constructor privado
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

    @JsonProperty("tipo")
    public String getTipo() {
        return "anonimo";
    }
}