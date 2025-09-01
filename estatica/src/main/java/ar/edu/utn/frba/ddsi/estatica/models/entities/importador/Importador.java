package ar.edu.utn.frba.ddsi.estatica.models.entities.importador;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.lector.Lector;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.lector.LectorCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

@Component
public class Importador {
    private final LectorCSV lectorCSV;
    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final Map<String, Lector> lectores = new HashMap<>();
    private List<Resource> archivosProcesados = new ArrayList<>();

    public Importador() {
        // TODO decidir cuando / donde instanciar los lectores
        this.lectorCSV = new LectorCSV();
        lectores.put("csv", lectorCSV);
        // En el futuro aca agregariamos mas importadores segun el tipo de archivo
        this.archivosProcesados = new ArrayList<>();
    }


    public List<Hecho> importarHechos() {
        List<Hecho> hechosImportados = new ArrayList<>();

//        for(Resource r : recursos) {
//            System.out.println("Recurso encontrado: " + r.getFilename());
//        }

        // Filtrar los archivos que no estan en archivosProcesados
        List<Resource> archivosNuevos = this.filtrarArchivosNuevos();

        try {
            for (Resource recurso : archivosNuevos) {
                    String filename = recurso.getFilename();
                    try {
                        List<Hecho> hechosDesdeArchivo = importarSegunArchivo(recurso);
                        hechosImportados.addAll(hechosDesdeArchivo);
                        this.archivosProcesados.add(recurso);
                    } catch (Exception e) {
                        System.err.println("Error al procesar el recurso " + filename + ": " + e.getMessage());
                    }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return hechosImportados;

    } // archivoProcesadoDTO importarHechos(List<String> nombresArchivosProcesados)

    public List<Resource> filtrarArchivosNuevos() {
        Resource[] archivosSinProcesar;
        try {
            archivosSinProcesar = resolver.getResources("classpath:archivos/*");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Resource> archivosNuevos = new ArrayList<>();
        for (Resource archivo : archivosSinProcesar) {
            if (!this.archivosProcesados.contains(archivo)) {
                archivosNuevos.add(archivo);
            }
        }
        return archivosNuevos;
    }




    private List<Hecho> importarSegunArchivo(Resource recurso) throws Exception {
        String extension = recurso.getFilename().substring(recurso.getFilename().lastIndexOf(".") + 1).toLowerCase();
        Lector lector = lectores.get(extension);
        if (lector == null) {
            throw new Exception("Formato de archivo no soportado: " + extension);
        }
        return lector.leerArchivo(recurso);
    }
}
    /*
    private List<Hecho> importarSegunArchivo(Resource recurso) throws Exception {
        String extensionArchivo = recurso.getFilename().substring(recurso.getFilename().lastIndexOf(".") + 1).toLowerCase();
        switch (extensionArchivo) {
            case "csv":
                return importadorCSV.importarCSV(recurso.getFilename());
            default:
                throw new Exception("Formato de archivo no soportado: " + extensionArchivo);
        }

    }
*/
