package ar.edu.utn.frba.ddsi.estadistica.models.repositories;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
