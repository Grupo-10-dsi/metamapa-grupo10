package ar.edu.utn.frba.ddsi.dinamica.controllers;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.dinamica.services.DinamicaService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/dinamica")
@RestController

public class DinamicaController {
    private final DinamicaService dinamicaService;

    public DinamicaController(DinamicaService dinamicaService) {
        this.dinamicaService = dinamicaService;
    }


    @PostMapping("/hechos/textual")
    public void crearHecho(@RequestBody HechoTextualDTO hechoDTO) {
        dinamicaService.crearHechoTextual(hechoDTO);
    }

    @PostMapping("/hechos/multimedia")
    public void crearHecho(@RequestBody HechoMultimediaDTO hechoDTO) {
        dinamicaService.crearHechoMultimedia(hechoDTO);
    }





}
