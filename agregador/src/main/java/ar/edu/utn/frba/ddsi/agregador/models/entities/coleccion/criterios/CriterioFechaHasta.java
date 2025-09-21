package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CriterioFechaHasta extends CriterioPertenencia{
    private final LocalDateTime hasta;

    public CriterioFechaHasta(LocalDateTime hasta) {
        this.hasta = hasta;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return hasta.isAfter(hecho.getFechaAcontecimiento());
    }

}
