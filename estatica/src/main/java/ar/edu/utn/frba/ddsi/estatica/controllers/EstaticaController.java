package ar.edu.utn.frba.ddsi.estatica.controllers;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.services.HechosServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequestMapping("/api/estatica")
@RestController
public class EstaticaController {

    private final HechosServices hechosServices;

    public EstaticaController(HechosServices hechosServices) {
        this.hechosServices = hechosServices;
    }

    @GetMapping("/hechos")
    public List<Hecho> listarHechos() {
        return hechosServices.obtenerHechos();
    }

}
