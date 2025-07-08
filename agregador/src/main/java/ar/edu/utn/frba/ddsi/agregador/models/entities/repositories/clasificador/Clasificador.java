package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.clasificador;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

public class Clasificador {
    private final Clasificador clasificador;

    public Clasificador(Clasificador clasificador) {
        this.clasificador = clasificador;
    }

    // Clasifica los hechos uniendo el título y la descripción, podemos modificarlo en un futuro
    public List<Hecho> clasificarHechosPorMenciones(List<Hecho> hechos) {
        Map<String, Long> conteoPorHecho = hechos.stream()
                .collect(Collectors.groupingBy(
                        hecho -> hecho.getTitulo() + "|" + hecho.getDescripcion(),
                        Collectors.counting()
                ));

        hechos.forEach(hecho -> {
            String clave = hecho.getTitulo() + "|" + hecho.getDescripcion();
            int menciones = conteoPorHecho.get(clave).intValue();
            hecho.setCantidadMenciones(menciones);
        });
        return hechos;
    }

}
