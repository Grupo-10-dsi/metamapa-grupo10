package ar.edu.utn.frba.ddsi.normalizador.models.entities.normalizador;

import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;

public class Normalizador {
    private final List<INormalizador> normalizadores = new ArrayList<>();

    private static Normalizador instance;

    private Normalizador() {// Constructor privado
    }

    public static Normalizador getInstance() {
        if (instance == null) {
            instance = new Normalizador();
        }
        return instance;
    }

    public Hecho normalizar(Hecho hechoCrudo) {
        Hecho hechoNormalizado = normalizadores.stream().reduce(hechoCrudo,
                (hechoAcumulado, unNormalizador) -> unNormalizador.normalizar(hechoAcumulado),
                (hecho1, hecho2) -> hecho2
        );


        return hechoNormalizado;

        /*
         * Hecho hechoNormalizado = unHecho
         * for (INormalizador unNormalizador : normalizadores) {
         *   hechoNormalizado = unNormalizador.normalizar(hechoNormalizado)
         * }
         * return hechoNormalizado;
         *
         */
    }
}
