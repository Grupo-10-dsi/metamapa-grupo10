package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrigenFuenteRepository extends JpaRepository<OrigenFuente, Integer> {


    OrigenFuente findOrigenFuenteById(Integer id);


}
