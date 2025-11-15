package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

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

}