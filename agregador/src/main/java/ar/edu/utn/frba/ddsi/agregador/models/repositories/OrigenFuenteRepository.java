package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrigenFuenteRepository extends JpaRepository<OrigenFuente, Integer> {

}
