package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.importador.Importador;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class HechosRepository {

    private final List<Hecho> hechos = new ArrayList<>();
    //TODO: Ver como es la demo
    List<String> urls = List.of("estatica/hechos", "dinamica/hechos", "proxy/metamapa/hechos");
    private final Importador importador= new Importador(urls);

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

    public List<Hecho> importarHechosDesdeFuentes() {
        return importador.importarHechos();

    }
}
