package ar.edu.utn.frba.ddsi.estadistica.models.entities;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalTime;
import java.util.List;

public class AgregadorClient {
    private final WebClient webClient;

    public AgregadorClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // Trae la ubicacion que esta en la mayor cantidad de hechos de una coleccion
    public List<Ubicacion> obtenerUbicacionesDeColeccion (Integer Id) {
        List<Ubicacion> ubicacionesConMasHechos = webClient.get()
                .uri("/estadisticas/coleccion/{Id}/ubicaciones", Id)
                .retrieve()
                .bodyToFlux(Ubicacion.class)
                .collectList()
                .block();
        if(ubicacionesConMasHechos == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la provincia o la coleccion buscada");
        }
        return ubicacionesConMasHechos;
    }

    public List<Coleccion> obtenerTodasLasColecciones() {
        List<Coleccion> colecciones = webClient.get()
                .uri("/colecciones")
                .retrieve()
                .bodyToFlux(Coleccion.class)
                .collectList()
                .block();
        if(colecciones == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron colecciones");
        }
        return colecciones;
    }

    public Categoria obtenerCategoriaConMasHechos() {
        System.out.println("Llamando a la API de agregador para obtener la categoria con mas hechos...\n");
        Categoria categoriaConMasHechos = webClient.get()
                                .uri("/estadisticas/hechos/max-categoria")
                                .retrieve()
                                .bodyToMono(Categoria.class)
                                .block();
                
        if(categoriaConMasHechos == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la categoria buscada");
        }
        return categoriaConMasHechos;
    }

    // Trae la ubicacion que esta en la mayor cantidad de hechos de una categoria
    public List<Ubicacion> obtenerUbicacionesDeCategoria (Integer Id) {
        List<Ubicacion> ubicaciones = webClient.get()
                .uri("/estadisticas/categoria/{Id}/ubicaciones", Id)
                .retrieve()
                .bodyToFlux(Ubicacion.class)
                .collectList()
                .block();
        if(ubicaciones == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la provincia o la categoria buscada");
        }
        return ubicaciones;
    }

    public List<Categoria> obtenerTodasLasCategorias() {
        List<Categoria> categorias = webClient.get()
                .uri("/categorias")
                .retrieve()
                .bodyToFlux(Categoria.class)
                .collectList()
                .block();
        if(categorias == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron categorias");
        }
        return categorias;
    }

    public LocalTime obtenerHoraMasFrecuenteDeCategoria (Integer Id) {
        LocalTime horaMasFrecuente = webClient.get()
                .uri("/estadisticas/categoria/{Id}/hora", Id)
                .retrieve()
                .bodyToMono(LocalTime.class)
                .block();
        if(horaMasFrecuente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la hora buscada");
        }
        return horaMasFrecuente;
    }

    public List<SolicitudDTO> obtenerSolicitudesSpam() {
        List <SolicitudDTO> solicitudesSpam = webClient.get()
                .uri("/estadisticas/solicitudes/spam")
                .retrieve()
                .bodyToFlux(SolicitudDTO.class)
                .collectList()
                .block();
        if(solicitudesSpam == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la cantidad de solicitudes spam");
        }
        return solicitudesSpam;
    }

}
