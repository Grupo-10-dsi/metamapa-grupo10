package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CriterioFechaDesde extends CriterioPertenencia{
    private final LocalDateTime desde;

    public CriterioFechaDesde(LocalDateTime desde) {
        this.desde = desde;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return desde.isBefore(hecho.getFechaAcontecimiento());
    }
}
