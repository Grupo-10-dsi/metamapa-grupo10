package ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.util.List;


public class LectorCSV {
    public List<String[]> leerCSV(String nombreArchivo) throws IOException, CsvValidationException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nombreArchivo);

        if (inputStream == null) {
            throw new IOException("No se encontró el archivo en resources: " + nombreArchivo);
        }
        List<String[]> filas = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] campos;
            boolean primeraLinea = true;

            while ((campos = reader.readNext()) != null) {
                if (primeraLinea) {
                    primeraLinea = false; // saltar encabezado
                    continue;
                }
                filas.add(campos); // cada campos es un String[]
            }
        }

        return filas;
    }
}

