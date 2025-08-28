package ar.edu.utn.frba.ddsi.normalizador.models.repositories;

import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Categoria;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

@Repository
public class EquivalenciasRepository {
    @Getter
    private Map<String, Categoria> equivalenciasCategorias;
    private final ObjectMapper objectMapper;


    @Autowired
    public EquivalenciasRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, Categoria> obtenerEquivalencias() {
        return equivalenciasCategorias;
    }

    @PostConstruct
    public void cargarEquivalencias() {
        try {
            // Cargar el archivo desde resources
            Resource resource = new ClassPathResource("equivalencias.json");

            // Leer el JSON como un mapa de String a String
            Map<String, String> mapaJson = objectMapper.readValue(resource.getInputStream(), // TODO: DESNEGRIZAR
                    new TypeReference<Map<String, String>>() {});

            // Inicializar el mapa de equivalencias
            equivalenciasCategorias = new HashMap<>();

            // Convertir las entradas del JSON a objetos Categoria
            mapaJson.forEach((clave, valor) -> {
                Categoria categoria = new Categoria();
                categoria.setDetalle(valor);
                equivalenciasCategorias.put(clave, categoria);
            });



        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de equivalencias", e);
        }
    }

    public void agregarCategoriaNueva(Categoria nuevaCategoria) {
        equivalenciasCategorias.put(nuevaCategoria.getDetalle().toLowerCase(), nuevaCategoria);
    }

}
