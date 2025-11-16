package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Contribuyente;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName("registrado")
public class Registrado extends Contribuyente {

    private Integer id;
    private String nombre;


    public Registrado(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }
}
