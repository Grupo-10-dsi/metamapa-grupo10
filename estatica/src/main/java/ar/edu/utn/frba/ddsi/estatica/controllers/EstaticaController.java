package ar.edu.utn.frba.ddsi.estatica.controllers;

import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.services.HechosServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<ArchivoProcesadoDTO> listarHechos(@RequestParam() List<String> archivosProcesados) {
        return hechosServices.obtenerHechos(archivosProcesados);
    }

    // get /hechos/desastre1&desastre2&desastre3



}
