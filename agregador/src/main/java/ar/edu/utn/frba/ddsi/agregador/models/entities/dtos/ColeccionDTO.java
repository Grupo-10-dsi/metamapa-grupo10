package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;
import java.util.List;

@Getter
public class ColeccionDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Algoritmo_Consenso algoritmo_consenso;
    private List<String> urls_fuente;
    private List<CriterioDTO> criterios;

    public ColeccionDTO(Integer id, String titulo, String descripcion, Algoritmo_Consenso algoritmo_consenso, List<String> urls_fuente, List<CriterioDTO> criterios) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.algoritmo_consenso = algoritmo_consenso;
        this.urls_fuente = urls_fuente;
        this.criterios = criterios;
    }
}
