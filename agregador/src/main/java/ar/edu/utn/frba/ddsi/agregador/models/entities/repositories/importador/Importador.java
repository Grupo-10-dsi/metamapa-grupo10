package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.importador;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.conversor.Conversor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Importador {

    private LocalDate ultimaConsulta;
    protected WebClient webClient;
    private final Conversor adaptador = new Conversor();

    public Importador() {
        this.webClient = WebClient.builder()
                .baseUrl("")
                .build();
        this.ultimaConsulta = null;
    }

    public void importarHechos(Fuente fuente) {
        //String uri = aplicarFiltros(fuente.getUrl());
        String uri = fuente.getUrl();
        List<HechoDTO> hechos = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();

        if (hechos != null) {
            fuente.setHechos(hechos.stream().map(adaptador::convertirHecho).toList());
        }

        this.ultimaConsulta = LocalDate.now();
    }

    public String aplicarFiltros(String url){
        if( this.ultimaConsulta == null) { //Para la primera vez que consultamos
            return url;
        }

        String ultimaConsultaString = this.ultimaConsulta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return UriComponentsBuilder
                .fromPath(url)
                .queryParam("ultimaConsulta", ultimaConsultaString)
                .build()
                .toUriString();
    }
}








