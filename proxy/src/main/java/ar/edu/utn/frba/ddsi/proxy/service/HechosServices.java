package ar.edu.utn.frba.ddsi.proxy.service;


import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.metaMapa.MetaMapaClient;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HechosServices {
    private static Logger log = LoggerFactory.getLogger(HechosServices.class);
    private final HechosRepository hechosRepository;
    private static List<MetaMapaClient> instanciasMetaMapa = new ArrayList<>();

    @Value("${metamapa.instancia.url}")
    private String metaMapaUrl;

    @PostConstruct
    public void inicializarInstancias() {
        this.instanciasMetaMapa.add(new MetaMapaClient(metaMapaUrl));
    }

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> findAllHechos(String ultimaConsulta) {
        log.info("Buscando todos los hechos desde: {}", ultimaConsulta);
        List<Hecho> hechos = new ArrayList<>(this.hechosRepository.findAll());
        hechos.addAll(this.obtenerHechosMetaMapa(
                new FiltroRequest(null, null, null, null, null, null, null, ultimaConsulta)));

        return hechos.stream()
                .filter(hecho -> ultimaConsulta == null ||
                        hecho.getFechaCarga().isAfter(LocalDateTime.parse(ultimaConsulta)))
                .collect(Collectors.toList());
    }

    public List<Hecho> obtenerHechos(String nombreConexion) {
        log.info("Buscando hechos para: {}", nombreConexion);
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
        return this.instanciasMetaMapa.get(0).crearSolicitudDeEliminacion(idHecho, justificacion);
    }
}
