package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

import java.time.LocalDate;

public class CriterioFechaHasta implements CriterioPertenencia{
    private LocalDate hasta;

    public CriterioFechaHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return hasta.isAfter(hecho.getFechaAcontecimiento());
    }

}
