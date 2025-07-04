package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios.CriterioPertenencia;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class Coleccion {
    private UUID id;
    private String titulo;
    private String descripcion;
    private Algoritmo_Consenso algoritmo_consenso;
    private List<Fuente> fuentes;
    private List<CriterioPertenencia> criterios;

    public Coleccion(String titulo, String  descripcion, Algoritmo_Consenso algoritmo_consenso, List<Fuente> fuentes, List<CriterioPertenencia> criterios) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.algoritmo_consenso = algoritmo_consenso;
        this.fuentes = fuentes;
        this.criterios = criterios;

    }

    public List<Hecho> mostrarHechos() {
        return this.fuentes.stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .toList();
    }




    public boolean vericidad(String algoritmo) {
        // Verifica la veracidad de los hechos en la coleccion
        switch (algoritmo) {
            case "multiplesMenciones":
                System.out.println("Usando algoritmo de múltiples menciones");
                break;
            case "mayoriaSimple":
                System.out.println("Usando algoritmo de mayoría simple");
                break;
            case "absoluta":
                System.out.println("Usando algoritmo absoluto");
                break;
            default:
                System.out.println("Algoritmo por defecto");
        }
        return true; //TODO: IMPLEMENTAR ESTO
    }
}
