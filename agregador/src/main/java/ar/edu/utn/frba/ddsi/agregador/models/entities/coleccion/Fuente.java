package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;

import java.util.List;
@Getter
public class Fuente {
    private List<Hecho> hechos = List.of();
    private final String url;
    private final String nombre;

    public Fuente(String url, String nombre) {
        this.url = url;
        this.nombre = nombre;
    }

    public void setHechos(List<Hecho> hechos) {
        this.hechos = hechos;
    }

}
