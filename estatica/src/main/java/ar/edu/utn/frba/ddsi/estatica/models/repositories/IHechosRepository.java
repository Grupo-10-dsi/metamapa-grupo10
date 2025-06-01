package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;


public interface IHechosRepository {
    List<Hecho> findAll();
    Hecho findById(UUID id);
    void save(Hecho hecho);
    void delete(Hecho hecho);
    void addHechos(List<Hecho> hechos);
}
