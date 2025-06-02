package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.*;

@Repository
public class HechosRepository {

    private final List<Hecho> hechos = new ArrayList<Hecho>();

    public List<Hecho> findAll() {
        return this.hechos;
    }

    public Hecho findById(UUID id) {
        return this.hechos.stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Hecho hecho) {
        this.hechos.add(hecho);
    }

    public void delete(Hecho hecho) {
        this.hechos.removeIf(h -> h.getId().equals(hecho.getId()));
    }

    public void addHechos(List<Hecho> hechos) {
        this.hechos.addAll(hechos);
    }

}
