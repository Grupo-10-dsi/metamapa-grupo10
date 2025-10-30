package ar.edu.utn.frba.ddsi.estadistica.service;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.SolicitudDTO;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CSVService {


    public ResponseEntity<?> convertirProvinciasACSV(List<String> resultados, String nombreArchivo) {
        StringBuilder csvBuilder = new StringBuilder();
        boolean masDeUnResultado = resultados != null && resultados.size() > 1;
        csvBuilder.append(masDeUnResultado ? "Provincias\n" : "Provincia\n");
        for (String provincia : resultados) {
            csvBuilder.append(provincia).append("\n");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+nombreArchivo+".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBuilder.toString());
    }

    public ResponseEntity<?> convertirHorasACSV(List<LocalTime> resultados, String nombreArchivo) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Hora/s\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (LocalTime hora : resultados) {
            csvBuilder.append(hora.format(formatter)).append("\n");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+nombreArchivo+".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBuilder.toString());
    }


    public ResponseEntity<?> convertirACSV(List<Categoria> resultado, String nombreArchivo) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("idCategoria,detalle\n");
        for(Categoria categoria : resultado) {
            csvBuilder.append(categoria.getId())
                    .append(",")
                    .append(categoria.getDetalle())
                    .append("\n");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+nombreArchivo+".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBuilder.toString());
    }

    public ResponseEntity<?> convertirSolicitudesACSV(List<SolicitudDTO> resultado, String nombreArchivo) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("idSolicitud,idHecho,justificacion,estadoSolicitud\n");
        for(SolicitudDTO solicitud : resultado) {
            csvBuilder.append(solicitud.getId())
                    .append(",")
                    .append(solicitud.getIdHecho())
                    .append(",")
                    .append(solicitud.getJustificacion())
                    .append(",")
                    .append(solicitud.getEstadoSolicitud())
                    .append("\n");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+nombreArchivo+".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBuilder.toString());
    }

    public CSVService() {
    }
}
