package ar.edu.utn.frba.ddsi.estadistica.models.repositories;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Hora_Frecuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoraFrecuenteRepository extends JpaRepository<Hora_Frecuente, Integer> {
}
