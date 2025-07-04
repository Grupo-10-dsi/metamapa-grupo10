package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

import java.time.LocalDate;

public class CriterioFechaDesde implements CriterioPertenencia{
    private LocalDate desde;

    public CriterioFechaDesde(LocalDate desde) {
        this.desde = desde;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return desde.isBefore(hecho.getFechaAcontecimiento());
    }
}
