package ar.edu.utn.frba.ddsi.proxy.metaMapa;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class MetaMapaClient {
    private final WebClient webClient;

    public MetaMapaClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl) // Cambialo por la URL real
                .build();
    }

    public List<Hecho> obtenerHechos(FiltroRequest filtro) {

        String uri = filtro.aplicarFiltroARequest(UriComponentsBuilder
                .fromPath("/hechos")); //Por ejemplo, podría agregar parámetros como ?tipo=evento&fecha=2025.

        return webClient.get()                 // Inicia construcción de un GET
                .uri(uri)                      // Usa la URI construida con filtros
                .retrieve()                    // Realiza la solicitud (envía el GET)
                .bodyToFlux(Hecho.class)      // Convierte la respuesta JSON en un flujo de objetos `Hecho`
                .collectList()                // Junta el flujo en una lista
                .block();                     // Bloquea y espera la respuesta (estilo imperativo)
    }
}
