package ar.edu.utn.frba.ddsi.proxy.service;


import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.metaMapa.MetaMapaClient;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HechosServices {
    private final HechosRepository hechosRepository;
    private static List <MetaMapaClient> instanciasMetaMapa = new ArrayList<>();

    @PostConstruct
    public void inicializarInstancias() {
        this.instanciasMetaMapa.add(new MetaMapaClient("http//localhost:8081"));
        //this.instanciasMetaMapa.add(new MetaMapaClient("http//localhost:8082"));
    }
    private final MetaMapaClient instanciaMetaMapa3 = new MetaMapaClient("http://localhost:8083");

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> obtenerHechos(String nombreConexion) {
        System.out.println("Buscando hechos para: " + nombreConexion);
        List<Hecho> hechos = this.hechosRepository.findByName(nombreConexion);
        if (hechos == null || hechos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron hechos para el nombre: " + nombreConexion);
        }
        return hechos;
    }


    @Scheduled(fixedRate = 60 * 60 * 1000)  //cada 1 hora
    public void actualizarHechosPeriodicamente() {
        this.hechosRepository.obtenerHechos();
    }

    public List<Hecho> obtenerHechosMetaMapa(FiltroRequest query) {
        return instanciasMetaMapa.stream()
                .flatMap(instanciaMetaMapa -> instanciaMetaMapa.obtenerHechos(query).stream())
                .toList();
    }

    public List<Hecho> obtenerHechosPorColeccion(FiltroRequest query, String handle) {
        return instanciasMetaMapa.stream()
                .flatMap(instanciaMetaMapa -> instanciaMetaMapa.obtenerHechosPorColeccion(query, handle).stream())
                .toList();
    }

    public SolicitudEliminacion crearSolicitudDeEliminacion(UUID idHecho, String justificacion) {
        // Faltaria logica para distinguir entre las instancias de MetaMapa
        return this.instanciasMetaMapa.get(0).crearSolicitudDeEliminacion(idHecho, justificacion);
    }
}
