package ar.edu.utn.frba.ddsi.proxy.service;


import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.metaMapa.MetaMapaClient;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HechosServices {
    private final HechosRepository hechosRepository;
    private MetaMapaClient instanciaMetaMapa;

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> obtenerHechos(String nombre) {
        return this.hechosRepository.findByName(nombre);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // cada 1 hora
    public void actualizarHechosPeriodicamente() {
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
