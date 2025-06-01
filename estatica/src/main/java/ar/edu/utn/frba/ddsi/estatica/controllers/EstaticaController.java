package ar.edu.utn.frba.ddsi.estatica.controllers;

import ar.edu.utn.frba.ddsi.estatica.services.IHechosServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/estatica")

public class EstaticaController {

    private final IHechosServices hechosServices;

    public EstaticaController(IHechosServices hechosServices) {
        this.hechosServices = hechosServices;
    }

    @GetMapping
    public ResponseEntity<?> ObtenerHechos() {
        return ResponseEntity.ok(hechosServices.obtenerHechos());
    }







}
