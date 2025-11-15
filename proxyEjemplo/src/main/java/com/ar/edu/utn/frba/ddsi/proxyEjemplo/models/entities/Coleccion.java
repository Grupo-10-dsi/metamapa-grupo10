package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Hecho;
import java.time.LocalDate;
import java.util.List;

public class Coleccion {
    private String id;
    private List<Hecho> hechos;

    public Coleccion(String id, List<Hecho> hechos) {
        this.id = id;
        this.hechos = hechos;
    }

    public String getId() {
        return id;
    }

    public List<Hecho> getHechos() {
        return hechos;
    }
}