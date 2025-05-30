package domain;

import java.util.ArrayList;
/**
 * Clase Coleccion que representa una colección de hechos.
 * Contiene una fuente, un título, una descripción y criterios de pertenencia.
 */

public class Coleccion {
    private Fuente fuente;
    private String titulo;
    private String descripcion;
    private List<CriterioPertenencia> criteriosPertenencia;
    private List<Hecho> hechos;

    public void agregarHecho(Hecho hecho) {
        // TODO Implementar Lógica para agregar un hecho a la colección
    }
}