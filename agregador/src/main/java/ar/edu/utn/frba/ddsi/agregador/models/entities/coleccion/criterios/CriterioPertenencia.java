package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

public interface CriterioPertenencia {

    boolean cumpleConCriterio(Hecho hecho);
}


/*
* hechosParaColeccion = hechos.stream().filter(coleccion.getCriterioPertenencia()::cumpleConCriterio).toList();
*
* */