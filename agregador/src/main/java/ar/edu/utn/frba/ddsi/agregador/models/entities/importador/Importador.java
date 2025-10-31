package ar.edu.utn.frba.ddsi.agregador.models.entities.importador;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ArchivoProcesadoRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.CategoriaRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ContribuyenteRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.OrigenFuenteRepository;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

public class Importador {

    protected WebClient webClient;
    private final Conversor conversor = new Conversor();

    final int size = (int) DataSize.ofMegabytes(16).toBytes();
    final ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();

    public Importador() {
        this.webClient = WebClient.builder()
                .baseUrl("")
                .exchangeStrategies(strategies)
                .build();

    }

    public void importarHechos(Fuente fuente, LocalDateTime ultimaConsulta, ContribuyenteRepository contribuyenteRepository, ArchivoProcesadoRepository archivoProcesadoRepository, OrigenFuenteRepository origenFuenteRepository, CategoriaRepository categoriaRepository) {
        URI uri = aplicarUltimaConsulta(fuente, ultimaConsulta);

        //System.out.println(uri);
        fuente.realizarConsulta(uri, webClient, conversor, contribuyenteRepository, archivoProcesadoRepository, origenFuenteRepository, categoriaRepository);

    }

    public URI aplicarUltimaConsulta(Fuente fuente, LocalDateTime ultimaConsulta) {

        UriComponentsBuilder builder = fuente.armarParametrosConsulta(ultimaConsulta);

        return builder.build().toUri();
    }
}








