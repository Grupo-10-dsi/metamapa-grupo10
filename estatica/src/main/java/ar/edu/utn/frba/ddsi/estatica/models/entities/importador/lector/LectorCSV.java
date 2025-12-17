package ar.edu.utn.frba.ddsi.estatica.models.entities.importador.lector;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.HechoCSV;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV implements Lector {

    private static Logger log = LoggerFactory.getLogger(LectorCSV.class);

    @Override
    public List<Hecho> leerArchivo(Resource recurso) throws IOException, CsvValidationException {
        List<String[]> filas = this.leerCSV(recurso);
        List<Hecho> hechosImportados = new ArrayList<>();

        for (String[] fila : filas) {
            Hecho hechoImportado = this.obtenerHechoPorFila(fila);
            hechosImportados = this.findOrUpdateByTitulo(hechoImportado, hechosImportados);
        }
        if (hechosImportados.isEmpty()) {
            log.debug("No se encontraron hechos en el archivo: {}", recurso.getFilename());
        } else {
            log.debug("Se importaron {} hechos desde el archivo: {} ", hechosImportados.size(), recurso.getFilename());
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
                hecho.setDescripcion(hechoImportado.getDescripcion());
                hecho.setCategoria(hechoImportado.getCategoria());
                hecho.setUbicacion(hechoImportado.getUbicacion());
                hecho.setFechaAcontecimiento(hechoImportado.getFechaAcontecimiento());
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            hechos.add(hechoImportado);
        }

        return hechos;
    }

    public List<String[]> leerCSV(Resource recurso) throws IOException, CsvValidationException {
        InputStream inputStream = recurso.getInputStream();

        List<String[]> filas = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] campos;
            boolean primeraLinea = true;

            while ((campos = reader.readNext()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                filas.add(campos);
            }
        }

        return filas;
    }
}
