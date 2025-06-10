package ar.edu.utn.frba.ddsi.proxy.controllers;

import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frba.ddsi.proxy.service.HechosServices;

import java.util.List;

@RequestMapping("/api/proxy")
@RestController
public class ProxyController {
    private final HechosServices hechosServices;

    public ProxyController(HechosServices hechosServices) {
        this.hechosServices = hechosServices;
    }

    // 2. Como persona usuaria, quiero poder obtener todos los hechos de una fuente proxy demo configurada
    //  en una colección, con un nivel de antigüedad máximo de una hora.
    // 3. Como persona usuaria, quiero poder obtener todos los hechos de las fuentes MetaMapa
    //  configuradas en cada colección, en tiempo real.

    @GetMapping("demo/hechos")
    public List<Hecho> ObtenerHechosDemo() {
        String nombre = ""; // Agregar logica para obtener el nombre ¡
        return hechosServices.obtenerHechos(nombre);
    }

    @GetMapping("metaMapa/hechos")
    public List<Hecho> ObtenerHechosMetaMapa(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fecha_reporte_desde,
            @RequestParam(required = false) String fecha_reporte_hasta,
            @RequestParam(required = false) String fecha_acontecimiento_desde,
            @RequestParam(required = false) String fecha_acontecimiento_hasta,
            @RequestParam(required = false) String ubicacion
    ) {
        FiltroRequest filtros = new FiltroRequest(
                fecha_acontecimiento_hasta,
                ubicacion,
                fecha_acontecimiento_desde,
                fecha_reporte_hasta,
                fecha_reporte_desde,
                categoria
        );

        return hechosServices.obtenerHechosMetaMapa(filtros);
    }

    @GetMapping("metaMapa/colecciones/{identificador}/hechos")
    public List<Hecho> ObtenerHechosPorColeccion(
            @PathVariable String identificador,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fecha_reporte_desde,
            @RequestParam(required = false) String fecha_reporte_hasta,
            @RequestParam(required = false) String fecha_acontecimiento_desde,
            @RequestParam(required = false) String fecha_acontecimiento_hasta,
            @RequestParam(required = false) String ubicacion) {

        {
            FiltroRequest filtros = new FiltroRequest(
                    fecha_acontecimiento_hasta,
                    ubicacion,
                    fecha_acontecimiento_desde,
                    fecha_reporte_hasta,
                    fecha_reporte_desde,
                    categoria
            );
            return hechosServices.obtenerHechosPorColeccion(filtros, identificador);
        }
        // TODO POST /solicitudes
    }

