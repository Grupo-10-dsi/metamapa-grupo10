package ar.edu.utn.frba.ddsi.estatica.models.entities.importador;

import ar.edu.utn.frba.ddsi.estatica.models.entities.ArchivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.lector.Lector;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.lector.LectorCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

@Component
public class Importador {
    private final LectorCSV lectorCSV;
    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final Map<String, Lector> lectores = new HashMap<>();
    private List<String> archivosProcesados = new ArrayList<>();

    public Importador() {
        // TODO decidir cuando / donde instanciar los lectores
        this.lectorCSV = new LectorCSV();
        lectores.put("csv", lectorCSV);
        // En el futuro aca agregariamos mas importadores segun el tipo de archivo
        this.archivosProcesados = new ArrayList<>();
    }


    public List<ArchivoProcesado> importarHechos() {
        List<ArchivoProcesado> nuevosArchivosProcesados = new ArrayList<>();

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
                        nuevosArchivosProcesados.add(
                                new ArchivoProcesado(filename, LocalDateTime.now() , hechosDesdeArchivo)
                        );

                    } catch (Exception e) {
                        System.err.println("Error al procesar el recurso " + filename + ": " + e.getMessage());
                    }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nuevosArchivosProcesados;

    } // archivoProcesadoDTO importarHechos(List<String> nombresArchivosProcesados)

    public List<Resource> filtrarArchivosNuevos() {
        Resource[] archivosAlmacenados;
        try {
            archivosAlmacenados = resolver.getResources("classpath:archivos/*");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Resource> archivosNuevos = new ArrayList<>();
        /*
           for (Resource archivo : archivosAlmacenados) {
            if (!this.archivosProcesados.contains(archivo)) {
                archivosNuevos.add(archivo);
            }
        }*/

        archivosNuevos = Arrays.stream(archivosAlmacenados).filter(this::noProcesado).toList();

        return archivosNuevos;
    }

    private boolean noProcesado(Resource recurso) {
        System.out.println(recurso.getFilename());
        return !this.archivosProcesados.contains(recurso.getFilename());
    }


    private List<Hecho> importarSegunArchivo(Resource recurso) throws Exception {
        String extension = recurso.getFilename().substring(recurso.getFilename().lastIndexOf(".") + 1).toLowerCase();
        Lector lector = lectores.get(extension);
        if (lector == null) {
            throw new Exception("Formato de archivo no soportado: " + extension);
        }
        return lector.leerArchivo(recurso);
    }

    public void setArchivosProcesados(List<String> nombreArchivosProcesados) {
        this.archivosProcesados = nombreArchivosProcesados;
    }
}

    /*
    * public void setArchivosProcesados(List<String> nombreArchivosProcesados) {
        List<Resource> archivosProcesados;
        archivosProcesados = nombreArchivosProcesados.stream()
                .map(nombre -> {
                    try {
                        return resolver.getResource("classpath:archivos/" + nombre);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        this.archivosProcesados = archivosProcesados;
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
*/
