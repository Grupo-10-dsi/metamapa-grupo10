package ar.edu.utn.frba.ddsi.dinamica.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LectorCorpus {

    public static List<String> leerCorpus(String nombreArchivo) {
        InputStream inputStream = LectorCorpus.class.getClassLoader().getResourceAsStream(nombreArchivo);
        if (inputStream == null) {
            throw new RuntimeException("No se encontró el archivo en resources: " + nombreArchivo);
        }

        try (BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo: " + nombreArchivo, e);
        }
    }
}
