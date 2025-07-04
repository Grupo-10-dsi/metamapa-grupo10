package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

public class CriterioTitulo implements CriterioPertenencia {
    private String titulo;

    public CriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return hecho.getTitulo().equalsIgnoreCase(titulo);
    }

}
