package ar.edu.utn.frba.ddsi.proxy.models.repositories;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;

import java.util.ArrayList;
import java.util.List;

public class HechosRepository {
    private List<Hecho> hechos;

    public List<Hecho> findAll() {
        return this.hechos;
    }
}
