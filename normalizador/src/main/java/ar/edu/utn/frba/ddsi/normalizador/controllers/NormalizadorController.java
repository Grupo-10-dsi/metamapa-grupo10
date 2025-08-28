package ar.edu.utn.frba.ddsi.normalizador.controllers;

import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.normalizador.services.NormalizadorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/normalizador")
@Controller
public class NormalizadorController {
    private final NormalizadorService normalizadorService;

    public NormalizadorController(NormalizadorService normalizadorService) {
        this.normalizadorService = normalizadorService;
    }

    @GetMapping("/normalizar")
    public Hecho normalizar(@RequestParam() Hecho hechoCrudo) {
        return this.normalizadorService.normalizar(hechoCrudo);
    }

}
