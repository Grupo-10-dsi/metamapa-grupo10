package ar.edu.utn.frba.ddsi.proxy.service;


import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.metaMapa.MetaMapaClient;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Service
public class HechosServices {
    private final HechosRepository hechosRepository;
    private final MetaMapaClient instanciaMetaMapa = new MetaMapaClient("http://localhost:8081");

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> obtenerHechos(String nombre) {
        System.out.println("Buscando hechos para: " + nombre);
        List<Hecho> hechos = this.hechosRepository.findByName(nombre);
        if (hechos == null || hechos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron hechos para el nombre: " + nombre);
        }
        return hechos;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // cada 1 hora
    public void actualizarHechosPeriodicament() {
        this.hechosRepository.obtenerHechos();
    }

    public List<Hecho> obtenerHechosMetaMapa(FiltroRequest query) {
        return this.instanciaMetaMapa.obtenerHechos(query);
    }

    public List<Hecho> obtenerHechosPorColeccion(FiltroRequest query, String identificador) {
        return this.instanciaMetaMapa.obtenerHechosPorColeccion(query, identificador);
    }

    public SolicitudEliminacion crearSolicitudDeEliminacion(UUID idHecho, String justificacion) {
        return this.instanciaMetaMapa.crearSolicitudDeEliminacion(idHecho, justificacion);
    }
}
