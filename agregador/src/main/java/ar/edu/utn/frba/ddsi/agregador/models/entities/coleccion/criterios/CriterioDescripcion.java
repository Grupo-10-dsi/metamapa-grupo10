package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;

@Getter
public class CriterioDescripcion implements CriterioPertenencia{
    private final String descripcion;

    public CriterioDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean cumpleConCriterio(Hecho hecho) {
        return hecho.getDescripcion().equalsIgnoreCase(descripcion);
    }
}
