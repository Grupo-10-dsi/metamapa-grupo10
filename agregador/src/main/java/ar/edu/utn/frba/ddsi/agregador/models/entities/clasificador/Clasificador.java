package ar.edu.utn.frba.ddsi.agregador.models.entities.clasificador;


import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.HechosRepository;

public class Clasificador {

    public Clasificador() {
    }
    // Verifica si dos hechos son iguales comparando todos sus atributos
    public static boolean hechosIguales(Hecho hecho1, Hecho hecho2) {
        boolean titulosIguales = Objects.equals(hecho1.getTitulo(), hecho2.getTitulo());
        boolean descripcionesIguales = Objects.equals(hecho1.getDescripcion(), hecho2.getDescripcion());
        boolean categoriasIguales = Objects.equals(hecho1.getCategoria().getDetalle(), hecho2.getCategoria().getDetalle());
        boolean latitudesIguales = Objects.equals(hecho1.getUbicacion().getLatitud(), hecho2.getUbicacion().getLatitud());
        boolean longitudesIguales = Objects.equals(hecho1.getUbicacion().getLongitud(), hecho2.getUbicacion().getLongitud());
        boolean fechasIguales = Objects.equals(hecho1.getFechaAcontecimiento(), hecho2.getFechaAcontecimiento());
        boolean etiquetasIguales = compararEtiquetas(hecho1.getEtiquetas(), hecho2.getEtiquetas());


        return titulosIguales && descripcionesIguales && categoriasIguales
                && latitudesIguales && longitudesIguales && fechasIguales && etiquetasIguales;
    }

    // Clasifica los hechos uniendo el título y la descripción, podemos modificarlo en un futuro
    public static void clasificarHechosPorMenciones(List<Hecho> hechos, List<Fuente> fuentesRestantes, HechosRepository repoHechos) {
        for (Hecho hecho : hechos) {
            int mencionesHecho = compararHechos(hecho, fuentesRestantes, repoHechos);
            hecho.setCantidadMenciones(mencionesHecho);
        }
    }

    public static int compararHechos(Hecho hecho, List<Fuente> fuentesRestantes, HechosRepository repoHechos) {
        int cantidadMenciones = 1;
        for (Fuente fuente : fuentesRestantes) {
            boolean mencionadoEnFuente = repoHechos.findHechosByFuente(fuente)
                    .stream()
                    .anyMatch(h -> hechosIguales(h, hecho));

            if (mencionadoEnFuente) {
                cantidadMenciones += 1; // Solo suma 1 por fuente, sin importar duplicados
            }
        }
        return cantidadMenciones;
    }

    private static boolean compararEtiquetas(List<Etiqueta> etiquetas1, List<Etiqueta> etiquetas2) {
        // Si ambas son nulas, son iguales
        if (etiquetas1 == null && etiquetas2 == null) {
            return true;
        }

        // Si solo una es nula, son diferentes
        if (etiquetas1 == null || etiquetas2 == null) {
            return false;
        }

        // Si tienen diferentes tamaños, son diferentes
        if (etiquetas1.size() != etiquetas2.size()) {
            return false;
        }

        // Comparar descripciones de etiquetas independientemente del orden
        Set<String> descripciones1 = etiquetas1.stream()
                .filter(Objects::nonNull)
                .map(Etiqueta::getDescripcion)
                .collect(Collectors.toSet());

        Set<String> descripciones2 = etiquetas2.stream()
                .filter(Objects::nonNull)
                .map(Etiqueta::getDescripcion)
                .collect(Collectors.toSet());

        return descripciones1.equals(descripciones2);
    }

}
