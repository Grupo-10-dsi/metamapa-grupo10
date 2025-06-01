package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;


import java.util.List;
import java.util.UUID;

public class HechosRepository implements IHechosRepository{

    private List<Hecho> hechos;

    @Override
    public List<Hecho> findAll() {
        return this.hechos;
    }

    @Override
    public Hecho findById(UUID id) {
        return this.hechos.stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Hecho hecho) {
        this.hechos.add(hecho);
    }

    @Override
    public void delete(Hecho hecho) {
        this.hechos.removeIf(h -> h.getId().equals(hecho.getId()));
    }

    @Override
    public void addHechos(List<Hecho> hechos) {
        this.hechos.addAll(hechos);
    }


}
