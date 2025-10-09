package ar.edu.utn.frba.ddsi.estadistica.controllers;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Ubicacion;
import ar.edu.utn.frba.ddsi.estadistica.service.EstadisticaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalTime;
import java.util.List;

@RequestMapping("/api/estadisticas")
@RestController
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    //De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
    @GetMapping("/colecciones/{Id}/provincia-max-hechos")
    public String obtenerProvinciaDeColeccion(@PathVariable Integer Id) {
        return this.estadisticaService.obtenerProvinciaDeColeccion(Id);
    }

    //¿Cuál es la categoría con mayor cantidad de hechos reportados?
    @GetMapping("/hechos/max-categoria")
    public Categoria obtenerCategoriaConMasHechos() {
        return this.estadisticaService.obtenerCategoriaConMasHechos();
    }

    //¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/categoria/{id}/provincia-max-hechos")
    public String obtenerProvinciaDeCategoria(@PathVariable Integer id) {
        return this.estadisticaService.obtenerProvinciaDeCategoria(id);
    }

    //¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/categoria/{id}/hora")
    public LocalTime obtenerHoraMasFrecuente(@PathVariable Integer id) {
        return this.estadisticaService.obtenerHoraMasFrecuenteDeCategoria(id);
    }

    //¿Cuántas solicitudes de eliminación son spam?
    @GetMapping("/solicitudes/spam" )
    public Integer obtenerCantidadDeSolicitudesSpam() {
        return this.estadisticaService.obtenerCantidadDeSolicitudesSpam();
    }
}
