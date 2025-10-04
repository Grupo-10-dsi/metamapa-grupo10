package ar.edu.utn.frba.ddsi.agregador.models.entities.importador;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ContribuyenteRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

public class Importador {

    protected WebClient webClient;
    private final Conversor conversor = new Conversor();

    public Importador() {
        this.webClient = WebClient.builder()
                .baseUrl("")
                .build();

    }

    public void importarHechos(Fuente fuente, LocalDateTime ultimaConsulta, ContribuyenteRepository contribuyenteRepository) {
        URI uri = aplicarUltimaConsulta(fuente, ultimaConsulta);

        //System.out.println(uri);
        fuente.realizarConsulta(uri, webClient, conversor, contribuyenteRepository);

    }

    public URI aplicarUltimaConsulta(Fuente fuente, LocalDateTime ultimaConsulta) {

        UriComponentsBuilder builder = fuente.armarParametrosConsulta(ultimaConsulta);

        return builder.build().toUri();
    }
}








