package ar.edu.utn.frba.ddsi.estadistica.models.repositories;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Solicitud_Cantidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudCantidadRepository extends JpaRepository<Solicitud_Cantidad, Integer> {

}
