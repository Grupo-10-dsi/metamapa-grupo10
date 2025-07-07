package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.importador.Importador;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class HechosRepository {

    /**
     * Por ahora las fuentes se hardcodean aca. Eventualmente se
     * vera si se pueden agregar/sacar usando requests.
     */
    private final List<Fuente> fuentes = List.of(
            new Fuente("https://dinamica", "Dinamica"),
            new Fuente("https://estatica", "Estatica"),
            new Fuente("https://proxy", "Proxy")
    );

    private final Importador importador= new Importador();


    public Hecho findById(UUID id) {

        // Implementacion sin base de datos
        return fuentes.stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);

    }

    public Hecho findByIdAndUpdate(UUID id, Hecho updatedHecho) {
        Hecho existingHecho = findById(id);
        if (existingHecho != null) {
            // Logica sin base de datos
            fuentes.forEach(fuente -> {
                List<Hecho> hechos = fuente.getHechos();
                int index = hechos.indexOf(existingHecho);
                if (index != -1) {
                    hechos.set(index, updatedHecho);
                    fuente.setHechos(hechos);
                }
            });

        }
        return updatedHecho;
    }

    public List<Hecho> findAll() {
        return fuentes.stream().flatMap(fuente -> fuente.getHechos().stream())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void importarHechosDesdeFuentes() {
        fuentes.forEach(importador::importarHechos);

    }

    public List<Fuente> findFuentes(List<String> urls) {
        return fuentes.stream()
                .filter(fuente -> urls.contains(fuente.getUrl()))
                .toList();
    }

    public Long countFuentes() {
        return fuentes.stream().count();
    }
}
