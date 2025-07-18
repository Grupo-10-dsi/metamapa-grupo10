package ar.edu.utn.frba.ddsi.estatica.models.entities.importador;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV.ImportadorCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Clase singleton para importar desde distintos tipos de archivos
@Component
public class Importador {
    private final ImportadorCSV importadorCSV;
    private ResourceLoader resourceLoader;

    @Autowired
    public Importador(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.importadorCSV = new ImportadorCSV();
    }

    public List<Hecho> importarHechos() {
        List<Hecho> hechosImportados = new ArrayList<>();

        try {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader); // Es un buscador que encuentra los archivos con patron CSV

            String[] patrones = {"classpath:*.csv"};

            for (String patron : patrones) {
                Resource[] recursos = resolver.getResources(patron);

                Arrays.stream(recursos).forEach(recurso -> { // el forEach recorre cada recurso encontrado
                    try {
                        hechosImportados.addAll(importarSegunArchivo(recurso));
                    } catch (Exception e) {
                        System.err.println("Error al importar archivo " + recurso.getFilename() + ": " + e.getMessage());
                    }
                });
            }


        } catch (Exception e) {
            System.err.println("Error al buscar archivos CSV: " + e.getMessage());
        }

        return hechosImportados;

    }

    private List<Hecho> importarSegunArchivo(Resource recurso) throws Exception {
        String extensionArchivo = recurso.getFilename().substring(recurso.getFilename().lastIndexOf(".") + 1).toLowerCase();
        switch (extensionArchivo) {
            case "csv":
                return importadorCSV.importarCSV(recurso.getFilename());
            default:
                throw new Exception("Formato de archivo no soportado: " + extensionArchivo);
        }

    }


}
