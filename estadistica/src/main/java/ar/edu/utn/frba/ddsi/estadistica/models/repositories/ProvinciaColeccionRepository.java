package ar.edu.utn.frba.ddsi.estadistica.models.repositories;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.ProvinciaColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinciaColeccionRepository extends JpaRepository<ProvinciaColeccion, Integer> {
}
