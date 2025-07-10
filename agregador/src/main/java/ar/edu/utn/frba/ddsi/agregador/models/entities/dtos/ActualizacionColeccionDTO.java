package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import lombok.Getter;

import java.util.List;
@Getter
public class ActualizacionColeccionDTO {
    private Algoritmo_Consenso algoritmo_consenso;
    private List<String> urls_fuente;

    public ActualizacionColeccionDTO(Algoritmo_Consenso algoritmo_consenso, List<String> urls_fuente) {
        this.algoritmo_consenso = algoritmo_consenso;
        this.urls_fuente = urls_fuente;
    }
}
