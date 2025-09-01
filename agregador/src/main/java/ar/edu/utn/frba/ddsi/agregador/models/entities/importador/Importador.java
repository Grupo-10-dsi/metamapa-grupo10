package ar.edu.utn.frba.ddsi.agregador.models.entities.importador;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Importador {

    protected WebClient webClient;
    private final Conversor conversor = new Conversor();

    public Importador() {
        this.webClient = WebClient.builder()
                .baseUrl("")
                .build();

    }

    public void importarHechos(Fuente fuente, LocalDateTime ultimaConsulta) {
        URI uri = aplicarUltimaConsulta(fuente.getUrl(), ultimaConsulta);
        System.out.println(uri);
        List<HechoDTO> hechos = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();
        if (hechos != null) {
            fuente.agregarHechos(hechos.stream().map(conversor::convertirHecho).toList());
        }


        System.out.print("Ultima consulta: ");
        System.out.println(ultimaConsulta);
    }

    public URI aplicarUltimaConsulta(String url, LocalDateTime ultimaConsulta) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        if (ultimaConsulta != null) {
            String ultimaConsultaString = ultimaConsulta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            builder.queryParam("ultimaConsulta", ultimaConsultaString);
        }

        return builder.build().toUri();
    }
}








