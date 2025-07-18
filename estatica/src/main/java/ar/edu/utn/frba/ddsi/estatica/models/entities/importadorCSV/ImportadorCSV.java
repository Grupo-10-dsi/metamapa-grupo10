package ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.HechoCSV;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportadorCSV {
    private static final LectorCSV lector = new LectorCSV();

    ; // Cargador de recursos para acceder a los archivos CSV

//    public List<Hecho> cargarCSVs() {
//        List<Hecho> hechosImportados = new ArrayList<>();
//        try {
//            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader); // Es un buscador que encuentra los archivos con patron CSV
//            Resource[] recursos = resolver.getResources("classpath:*.csv"); // Busca todos los archivos CSV en el classpath
//
//
//            Arrays.stream(recursos).forEach(recurso -> { // el forEach recorre cada recurso (csv) encontrado
//                try {
//                    String nombreArchivo = recurso.getFilename();
//                    hechosImportados.addAll(this.importarHechosDesdeCSV(nombreArchivo));
//                } catch (Exception e) {
//                    System.err.println("Error al importar archivo " + recurso.getFilename() + ": " + e.getMessage());
//                }
//            });
//
//        } catch (Exception e) {
//            System.err.println("Error al buscar archivos CSV: " + e.getMessage());
//        }
//        return hechosImportados;
//    }

//    public List<Hecho> importarHechosDesdeCSV(String nombreArchivo) throws IOException, CsvValidationException {
//        ImportadorCSV importador = new ImportadorCSV();
//        List<Hecho> hechosImportados = importador.importarCSV(nombreArchivo);
//
//        if (hechosImportados.isEmpty()) {
//            System.out.println("No se encontraron hechos en el archivo: " + nombreArchivo);
//        } else {
//            System.out.println("Se importaron " + hechosImportados.size() + " hechos desde el archivo: " + nombreArchivo);
//        }
//        return hechosImportados;
//    }

    public List<Hecho> importarCSV(String nombreArchivo) throws IOException, CsvValidationException {
        List<String[]> filas = lector.leerCSV(nombreArchivo);
        List<Hecho> hechosImportados = new ArrayList<>();

        for (String[] fila : filas) {
            Hecho hechoImportado = this.obtenerHechoPorFila(fila);
            hechosImportados = this.findOrUpdateByTitulo(hechoImportado, hechosImportados);
        }
        if (hechosImportados.isEmpty()) {
            System.out.println("No se encontraron hechos en el archivo: " + nombreArchivo);
        } else {
            System.out.println("Se importaron " + hechosImportados.size() + " hechos desde el archivo: " + nombreArchivo);
        }

        return hechosImportados;
    }

    public Hecho obtenerHechoPorFila(String[] fila) {
        String titulo = fila[0].trim();
        String descripcion = fila[1].trim();
        String categoria = fila[2].trim();
        Double latitud = Double.parseDouble(fila[3].trim());
        Double longitud = Double.parseDouble(fila[4].trim());
        LocalDate fechaAcontecimientoCSV = LocalDate.parse(fila[5].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDateTime fechaAcontecimiento = fechaAcontecimientoCSV.atStartOfDay();


        HechoCSV hechoCSV = new HechoCSV(titulo, descripcion, new Categoria(categoria), new Ubicacion(latitud, longitud), fechaAcontecimiento);

        return new Hecho(
                hechoCSV.getTitulo(),
                hechoCSV.getDescripcion(),
                hechoCSV.getCategoria(),
                hechoCSV.getUbicacion(),
                hechoCSV.getFechaAcontecimiento()
        );
    }

    public List<Hecho> findOrUpdateByTitulo(Hecho hechoImportado, List<Hecho> hechos) {
        boolean encontrado = false;

        for (Hecho hecho : hechos) {
            if (hecho.getTitulo().equals(hechoImportado.getTitulo())) {
                // Actualizar el hecho existente con los nuevos datos
                hecho.setDescripcion(hechoImportado.getDescripcion());
                hecho.setCategoria(hechoImportado.getCategoria());
                hecho.setUbicacion(hechoImportado.getUbicacion());
                hecho.setFechaAcontecimiento(hechoImportado.getFechaAcontecimiento());
                encontrado = true;
                break;
            }
        }

        // Si no se encontró, añadir el nuevo hecho
        if (!encontrado) {
            hechos.add(hechoImportado);
        }

        return hechos;
    }
}
