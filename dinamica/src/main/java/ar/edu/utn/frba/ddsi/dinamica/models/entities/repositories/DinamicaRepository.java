package ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DinamicaRepository {

    private final List<Hecho> hechos = new ArrayList<>();

    public void save(Hecho hecho) {
        hechos.add(hecho);
    }


}
