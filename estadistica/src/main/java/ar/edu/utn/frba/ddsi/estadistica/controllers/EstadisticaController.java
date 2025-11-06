package ar.edu.utn.frba.ddsi.estadistica.controllers;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Provincia;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.SolicitudDTO;
import ar.edu.utn.frba.ddsi.estadistica.models.repositories.ProvinciaRepository;
import ar.edu.utn.frba.ddsi.estadistica.service.CSVService;
import ar.edu.utn.frba.ddsi.estadistica.service.EstadisticaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/estadisticas")
@RestController

@Tag(name = "Estadísticas", description = "Endpoints para obtener estadísticas de hechos y categorías")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;
    private final CSVService csvService;

    public EstadisticaController(EstadisticaService estadisticaService, CSVService csvService) {
        this.estadisticaService = estadisticaService;
        this.csvService = csvService;
    }

    //De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
    @GetMapping("/colecciones/provincia-max-hechos")
    @Operation(summary = "en qué provincia se agrupan la mayor cantidad de hechos reportados de una coleccion")
    public ResponseEntity<?> obtenerProvinciaDeColeccion(@RequestParam(required = false) Integer Id, @RequestParam(required = false) String formato, @RequestParam Integer cantidadProvincias) {

        List<String> resultados =  this.estadisticaService.obtenerProvinciaDeColeccion(Id, cantidadProvincias);
        if (formato != null && formato.equalsIgnoreCase("csv")){
            return this.csvService.convertirProvinciasACSV(resultados, "provincia_max_hechos-coleccion");
        }
        return ResponseEntity.ok(resultados);
    }

    //¿Cuál es la categoría con mayor cantidad de hechos reportados?
    @GetMapping("/hechos/max-categoria")
    @Operation(summary = "Categoría con más hechos reportados")
    public ResponseEntity<?> obtenerCategoriaConMasHechos(@RequestParam(required = false) String formato, @RequestParam(required = false) Integer cantidadCategorias) {
        List<Categoria> resultados =  this.estadisticaService.obtenerCategoriasConMasHechos(cantidadCategorias);

        if(formato != null && formato.equalsIgnoreCase("csv")){
            return this.csvService.convertirACSV(resultados, "categoria-max-hechos;");
        }

        return ResponseEntity.ok(resultados);
    }

    //¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/categoria/provincia-max-hechos")
    @Operation(summary = "en qué provincia se agrupan la mayor cantidad de hechos reportados de una categoria")
    public ResponseEntity<?> obtenerProvinciasDeCategoria(@RequestParam(required = false) Integer Id, @RequestParam(required = false) String formato, @RequestParam(required = false) Integer cantidadProvincias) {

        List<String> resultados = this.estadisticaService.obtenerProvinciasDeCategoria(Id, cantidadProvincias);

        if (formato != null && formato.equalsIgnoreCase("csv")){
            return this.csvService.convertirProvinciasACSV(resultados, "provincia_max_hechos-coleccion");
        }

        return ResponseEntity.ok(resultados);
    }

    //¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/categoria/hora")
    @Operation(summary = "A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría")
    public ResponseEntity<?> obtenerHoraMasFrecuente(@RequestParam(required = false)Integer Id, @RequestParam(required = false) String formato, @RequestParam(required = false) Integer cantidadHoras) {
        List<LocalTime> resultados = this.estadisticaService.obtenerHorasMasFrecuentesDeCategoria(Id, cantidadHoras);
        if (formato != null && formato.equalsIgnoreCase("csv")) {
            return this.csvService.convertirHorasACSV(resultados, "hora-hechos-max");
        }
        return ResponseEntity.ok(resultados);
    }


    //¿Cuántas solicitudes de eliminación son spam?
    @GetMapping("/solicitudes/spam" )
    @Operation(summary = "Cuantas solicitudes de eliminacion son spam")
    public ResponseEntity<?> obtenerCantidadDeSolicitudesSpam(@RequestParam(required = false, defaultValue = "false") boolean mostrar, @RequestParam(required = false) String formato) {
        List<SolicitudDTO> solicitudes = this.estadisticaService.obtenerCantidadDeSolicitudesSpam();
        if (mostrar && formato == null){
            return ResponseEntity.ok(solicitudes);
        }
        else if (mostrar && formato != null && formato.equalsIgnoreCase("csv")) {
            return this.csvService.convertirSolicitudesACSV(solicitudes, "solicitudes-spam");
        }
        return ResponseEntity.ok(solicitudes.size());
    }
}
