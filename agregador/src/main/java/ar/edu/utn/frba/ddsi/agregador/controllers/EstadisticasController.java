package ar.edu.utn.frba.ddsi.agregador.controllers;

//import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CiudadDTO;
//import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ProvinciaDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

//    @GetMapping("/colecciones/{coleccionID}/provincias")
//    public List<ProvinciaDTO> getProvinciasColeccion(@PathVariable UUID coleccionID) { // TODO si cambiamos tipo id, cambia esto
//        return null;
//    }
//
//    @GetMapping("/colecciones/{coleccionID}/ciudades")
//    public List<CiudadDTO> getCiudadesColeccion(@PathVariable UUID coleccionID) { // TODO si cambiamos tipo id, cambia esto
//        return null;
//    }
}
