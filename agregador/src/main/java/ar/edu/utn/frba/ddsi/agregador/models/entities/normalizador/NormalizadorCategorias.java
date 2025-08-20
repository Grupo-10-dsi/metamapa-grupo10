package ar.edu.utn.frba.ddsi.agregador.models.entities.normalizador;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.EquivalenciasRepository;
import java.util.Map;

public class NormalizadorCategorias {
    private final EquivalenciasRepository equivalenciasCategoriasRepo;

    public NormalizadorCategorias(EquivalenciasRepository equivalenciasCategoriasRepo) {
        this.equivalenciasCategoriasRepo = equivalenciasCategoriasRepo;
    }

    public Hecho normalizar (Hecho unHecho) {
        Map<String, Categoria> mapaDeEquivalencias = equivalenciasCategoriasRepo.obtenerEquivalencias();
        Categoria categoriaAsociada = mapaDeEquivalencias.get(unHecho.getCategoria().getDetalle());

        if(categoriaAsociada != null) {
            unHecho.setCategoria(categoriaAsociada);
        } else {
            equivalenciasCategoriasRepo.agregarCategoriaNueva(unHecho.getCategoria());
        }
        //commit

        return unHecho;
    }
}
