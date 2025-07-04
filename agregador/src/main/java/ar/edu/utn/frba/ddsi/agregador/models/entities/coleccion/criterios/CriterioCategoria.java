package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;


@Getter
public class CriterioCategoria implements CriterioPertenencia{
    private final Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return hecho.getCategoria().equals(categoria);
    }

}
