package ar.edu.utn.frba.ddsi.estadistica.models.entities;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public class AgregadorClient {
    private final WebClient webClient;

    public AgregadorClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String obtenerProvinciaDeColeccion (Integer Id) {
        String provinciaConMasHechos = webClient.get()
                .uri("/colecciones/{Id}/provincia-max-hechos", Id)
                .retrieve()
                .toString();
        if(provinciaConMasHechos == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la provincia buscada");
        }
        return provinciaConMasHechos;
    }

    public Categoria obtenerCategoriaConMasHechos() {
        Categoria categoriaConMasHechos = webClient.get()
                .uri("/hechos/max-categoria")
                .retrieve()
                .bodyToMono(Categoria.class)
                .block();
        if(categoriaConMasHechos == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la categoria buscada");
        }
        return categoriaConMasHechos;
    }

}
