package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class Coleccion {
    private UUID id;
    private List<Hecho> hechos;

    public Coleccion(List<Hecho> hechos) {
        this.hechos = hechos;
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
