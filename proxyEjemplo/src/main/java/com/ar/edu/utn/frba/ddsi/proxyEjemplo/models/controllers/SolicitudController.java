package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.controllers;

import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.SolicitudEliminacion;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolicitudController {
    @PostMapping("/solicitudes")
    public SolicitudEliminacion crearSolicitud(@RequestBody SolicitudEliminacion solicitud) {
        return solicitud;
    }
}