package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.controllers;

import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.*;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.services.HechoServices;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Anonimo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class HechoController {

    private final HechoServices hechoServices;

    public HechoController(HechoServices hechoServices) {
        this.hechoServices = hechoServices;
    }



    @GetMapping("/hechos")
    public List<Hecho> getHechos(@RequestParam(required = false) String categoria,
                                 @RequestParam(required = false) String fecha_reporte_desde,
                                 @RequestParam(required = false) String fecha_reporte_hasta,
                                 @RequestParam(required = false) String fecha_acontecimiento_desde,
                                 @RequestParam(required = false) String fecha_acontecimiento_hasta,
                                 @RequestParam(required = false) String latitud,
                                    @RequestParam(required = false) String longitud,
                                 @RequestParam(required = false) String ultimaConsulta
    ) {

        return hechoServices.getHechos(fecha_acontecimiento_hasta,
                latitud,
                longitud,
                fecha_acontecimiento_desde,
                fecha_reporte_hasta,
                fecha_reporte_desde,
                categoria,
                ultimaConsulta);
    }

    @GetMapping("/hechos/{id}")
    public Hecho getHechoPorId(@PathVariable Integer id) {
        return hechoServices.getHechoPorId(id);
    }


    @GetMapping("/colecciones/{id}/hechos")
    public List<Hecho> getHechosPorColeccion(@PathVariable String id) {
        return hechoServices.getHechosPorColeccion(id);
    }

}