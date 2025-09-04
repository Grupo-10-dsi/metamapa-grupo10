package ar.edu.utn.frba.ddsi.normalizador.models.entities.normalizador;

import ar.edu.utn.frba.ddsi.normalizador.models.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.normalizador.models.repositories.EquivalenciasRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Component
public class NormalizadorCategorias implements INormalizador {
    private final EquivalenciasRepository equivalenciasCategoriasRepo;

    public NormalizadorCategorias(EquivalenciasRepository equivalenciasCategoriasRepo) {
        this.equivalenciasCategoriasRepo = equivalenciasCategoriasRepo;
    }

    public HechoDTO normalizar(HechoDTO hechoCrudo) {

        Map<String, Categoria> mapaDeEquivalencias = equivalenciasCategoriasRepo.obtenerEquivalencias();

        System.out.println("Categoria original: " + hechoCrudo.getCategoria().getDetalle());
        System.out.println("Mapa de equivalencias: \n" + mapaDeEquivalencias);
        if (mapaDeEquivalencias == null)
            System.out.println("MAPA DE EQUIVALENCIAS VACIO");

        Categoria categoriaAsociada = mapaDeEquivalencias.get(hechoCrudo.getCategoria().getDetalle().toLowerCase());

        if(categoriaAsociada != null) {
            hechoCrudo.setCategoria(categoriaAsociada);
        } else {
            // llamar al modo complejo que aplica un algoritmo...

            equivalenciasCategoriasRepo.agregarCategoriaNueva(hechoCrudo.getCategoria());
        }

        return hechoCrudo;
    }
}
