package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Entity
public class CriterioTitulo extends CriterioPertenencia {
    private String titulo;

    public CriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    public CriterioTitulo() {

    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return hecho.getTitulo().equalsIgnoreCase(titulo);
    }

}
