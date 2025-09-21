package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios.CriterioPertenencia;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter

public class Coleccion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Algoritmo_Consenso algoritmo_consenso;

    @ManyToMany
    @JoinTable(
            name = "coleccion_fuente",
            joinColumns = @JoinColumn(name = "coleccion_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_url", referencedColumnName = "url")
    )
    private List<Fuente> fuentes;

    @OneToMany
    @JoinColumn(name = "coleccion_id", referencedColumnName = "id")
    private List<CriterioPertenencia> criterios;

    public Coleccion(String titulo, String  descripcion, Algoritmo_Consenso algoritmo_consenso, List<Fuente> fuentes, List<CriterioPertenencia> criterios) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.algoritmo_consenso = algoritmo_consenso;
        this.fuentes = fuentes;
        this.criterios = criterios;

    }

    public List<Hecho> mostrarHechos() {
        return this.fuentes.stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .filter(hecho -> this.criterios.stream()
                .allMatch(criterio -> criterio.cumpleConCriterio(hecho)))
                .toList();

    }





//    public boolean vericidad(String algoritmo) {
//        // Verifica la veracidad de los hechos en la coleccion
//        switch (algoritmo) {
//            case "multiplesMenciones":
//                System.out.println("Usando algoritmo de múltiples menciones");
//                break;
//            case "mayoriaSimple":
//                System.out.println("Usando algoritmo de mayoría simple");
//                break;
//            case "absoluta":
//                System.out.println("Usando algoritmo absoluto");
//                break;
//            default:
//                System.out.println("Algoritmo por defecto");
//        }
//        return true; //TODO: IMPLEMENTAR ESTO
//    }

//    public void aplivarVerificacionAlgoritmoPorDefecto() {
//        this.fuentes.stream()
//                .flatMap(fuente -> fuente.getHechos().stream())
//                .forEach(hecho -> hecho.setVerificado(true));
//    }

    // aplicarVerifiacionAlgoritmo
    /*
    public void aplicarVerificacionAlgoritmo(String algoritmo) {
        Map<Hecho, Long> conteo = this.fuentes.stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .filter(hecho -> this.criterios.stream()
                        .allMatch(criterio -> criterio.cumpleConCriterio(hecho)))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        int totalFuentes = this.fuentes.size();

        conteo.entrySet().stream()
                .filter(entry -> {
                    long cantidad = entry.getValue();
                    switch (algoritmo) {
                        case "multiplesMenciones":
                            return cantidad > 1;
                        case "mayoriaSimple":
                            return cantidad > totalFuentes / 2;
                        case "absoluta":
                            return cantidad == totalFuentes;
                        default:
                            return false;
                    }
                })
                .map(Map.Entry::getKey)
                .forEach(hecho -> hecho.setVerificado(true));
    }

     */

    public List<Hecho> mostrarHechosPorConsenso(String algoritmo) {
        // Mapa que cuenta menciones de cada hecho
        Map<Hecho, Long> conteo = this.fuentes.stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .filter(hecho -> this.criterios.stream()
                        .allMatch(criterio -> criterio.cumpleConCriterio(hecho)))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting() // cuenta cuantas veces aparece cada hecho en el stream, nos tienen que decir cuando están repetidos para cambiarlo
                ));

        int totalFuentes = this.fuentes.size();

        return conteo.entrySet().stream() // itera todos los hechos
                .filter(entry -> {
                    long cantidad = entry.getValue(); // obtiene la cantidad de menciones del hecho
                    switch (algoritmo) {
                        case "multiplesMenciones":
                            return cantidad > 1;
                        case "mayoriaSimple":
                            return cantidad > totalFuentes / 2;
                        case "absoluta":
                            return cantidad == totalFuentes;
                        default:
                            return false; // algoritmo no reconocido
                    }
                })
                .map(Map.Entry::getKey) // obtiene el hecho de la entrada del mapa
                .toList();
    }

}
