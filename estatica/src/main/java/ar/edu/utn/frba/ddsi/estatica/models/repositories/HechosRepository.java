package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV.ImportadorCSV;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public class HechosRepository {

    private final List<Hecho> hechos = new ArrayList<Hecho>();
    private final ResourceLoader resourceLoader;

    public HechosRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public List<Hecho> findAll() {
        return importarTodosHechosDesdeCSV();
    }

    public Hecho findById(UUID id) {
        return this.hechos.stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Hecho hecho) {
        this.hechos.add(hecho);
    }

    public void delete(Hecho hecho) {
        this.hechos.removeIf(h -> h.getId().equals(hecho.getId()));
    }

    public void addHechos(List<Hecho> hechos) {
        this.hechos.addAll(hechos);
    }

    public List<Hecho> importarTodosHechosDesdeCSV() {
        List<Hecho> hechosImportados = new ArrayList<>();
        try {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader); // Es un buscador que encuentra los archivos con patron CSV
            Resource[] recursos = resolver.getResources("classpath:*.csv"); // Busca todos los archivos CSV en el classpath


            Arrays.stream(recursos).forEach(recurso -> {
                try {
                    String nombreArchivo = recurso.getFilename();
                    hechosImportados.addAll(this.importarHechosDesdeCSV(nombreArchivo));
                } catch (Exception e) {
                    System.err.println("Error al importar archivo " + recurso.getFilename() + ": " + e.getMessage());
                }
            });

        } catch (Exception e) {
            System.err.println("Error al buscar archivos CSV: " + e.getMessage());
        }
        return hechosImportados;
    }

    public List<Hecho> importarHechosDesdeCSV(String nombreArchivo) throws IOException, CsvValidationException {
        ImportadorCSV importador = new ImportadorCSV();
        List<Hecho> hechosImportados = importador.importarCSV(nombreArchivo);

        if (hechosImportados.isEmpty()) {
            System.out.println("No se encontraron hechos en el archivo: " + nombreArchivo);
            return hechosImportados;
        } else {
            System.out.println("Se importaron " + hechosImportados.size() + " hechos desde el archivo: " + nombreArchivo);
            return hechosImportados;
        }
    }
}
