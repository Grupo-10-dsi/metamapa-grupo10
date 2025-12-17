package ar.edu.utn.frba.ddsi.proxy.metaMapa;

import ar.edu.utn.frba.ddsi.proxy.models.entities.DTOS.HechoDto;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.proxy.models.entities.solicitudes.SolicitudEliminacion;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public class MetaMapaClient {
    private final WebClient webClient;

    public MetaMapaClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<Hecho> obtenerHechos(FiltroRequest filtro) {

        String uri = filtro.aplicarFiltroARequest(UriComponentsBuilder
                .fromPath("/hechos"));

        List<HechoDto> hechosMetaMapa = webClient.get()
                                        .uri(uri)
                                        .retrieve()
                                        .bodyToFlux(HechoDto.class)
                                        .collectList()
                                        .block();
        return hechosMetaMapa.stream().map(this::hechoFromDto).toList();
    }

    public Hecho hechoFromDto(HechoDto dto) {
        if(dto.getCuerpo() == null){
        return new HechoMultimedia(
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getCategoria(),
                dto.getUbicacion(),
                dto.getFechaAcontecimiento(),
                dto.getEtiquetas(),
                dto.getContribuyente(),
                dto.getContenidoMultimedia()
        );}
        else{
            return new HechoTextual(
                    dto.getTitulo(),
                    dto.getDescripcion(),
                    dto.getCategoria(),
                    dto.getUbicacion(),
                    dto.getFechaAcontecimiento(),
                    dto.getEtiquetas(),
                    dto.getContribuyente(),
                    dto.getCuerpo()
            );
        }
    }

    public List<Hecho> obtenerHechosPorColeccion(FiltroRequest filtro, String handle) {

        String uri = filtro.aplicarFiltroARequest(UriComponentsBuilder
                .fromPath("/colecciones/" + handle + "/hechos"));
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Hecho.class)
                .collectList()
                .block();
    }

    public SolicitudEliminacion crearSolicitudDeEliminacion(UUID idHecho, String justificacion) {
        Hecho hechoReferenciado = webClient.get()
                .uri("/hechos/"+ idHecho)
                .retrieve()
                .bodyToMono(Hecho.class)
                .block();
        if (hechoReferenciado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el hecho con ID: " + idHecho);
        }
        return webClient.post()
                .uri("/solicitudes/" + idHecho)
                .bodyValue(new SolicitudEliminacion(idHecho, justificacion))
                .retrieve()
                .bodyToMono(SolicitudEliminacion.class)
                .block();
    }

}
