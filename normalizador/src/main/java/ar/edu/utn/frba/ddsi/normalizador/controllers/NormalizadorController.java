package ar.edu.utn.frba.ddsi.normalizador.controllers;

import ar.edu.utn.frba.ddsi.normalizador.models.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.normalizador.services.NormalizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/normalizador")
@RestController
public class NormalizadorController {
    private final NormalizadorService normalizadorService;

    @Autowired
    public NormalizadorController(NormalizadorService normalizadorService) {
        this.normalizadorService = normalizadorService;
    }

    @PatchMapping("/normalizar")
    public HechoDTO normalizar(@RequestBody HechoDTO hechoCrudo) {
        //System.out.println("Recibio el hecho con categoria: " + hechoCrudo.getCategoria().getDetalle());
        return this.normalizadorService.normalizar(hechoCrudo);
    }

    @GetMapping("/health")
    public String health() {
        return "Servicio de normalizacion activo";
    }

}
