package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Fuente {
    private List<Hecho> hechos = new ArrayList<>();
    private final String url;
    private final String nombre;

    public Fuente(String url, String nombre) {
        this.url = url;
        this.nombre = nombre;
    }

    public void agregarHechos(List<Hecho> nuevosHechos) {
        this.hechos.addAll(nuevosHechos);
    }

    public void setHechos(List<Hecho> nuevosHechos) {
        this.hechos = nuevosHechos;
    }

}
