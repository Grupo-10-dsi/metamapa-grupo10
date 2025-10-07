package ar.edu.utn.frba.ddsi.estadistica.controllers;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.service.EstadisticaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/estadisticas")
@RestController
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/colecciones/{Id}/provincia-max-hechos")
    public String obtenerProvinciaDeColeccion(@PathVariable Integer Id) {
        return this.estadisticaService.obtenerProvinciaDeColeccion(Id);
    }

    @GetMapping("/hechos/max-categoria")
    public Categoria obtenerCategoriaConMasHechos() {
        return this.estadisticaService.obtenerCategoriaConMasHechos();
    }
}
