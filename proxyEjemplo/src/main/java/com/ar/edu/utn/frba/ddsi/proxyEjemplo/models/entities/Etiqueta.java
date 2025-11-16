package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Etiqueta {
    private String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }

    public Etiqueta() {}
}
