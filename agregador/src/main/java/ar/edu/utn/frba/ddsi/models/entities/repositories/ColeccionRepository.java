package ar.edu.utn.frba.ddsi.models.entities.repositories;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ColeccionRepository {

    private final List<Hecho> hechos = new ArrayList<>();

    public void save(Hecho hecho) {
        hechos.add(hecho);
    }

    public Hecho findById(UUID id) {
        return hechos.stream().filter(hecho -> hecho.getId().equals(id)).findFirst().orElse(null);
    }

    public Hecho findByIdAndUpdate(UUID id, Hecho updatedHecho) {
        Hecho existingHecho = findById(id);
        if (existingHecho != null) {
            // Logica sin base de datos
            int index = hechos.indexOf(existingHecho);
            hechos.set(index, updatedHecho);
            return updatedHecho;
        }
        return null;
    }

    public List<Hecho> findAll() {
        return hechos;
    }
}
