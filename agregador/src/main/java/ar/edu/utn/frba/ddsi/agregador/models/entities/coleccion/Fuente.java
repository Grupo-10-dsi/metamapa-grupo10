package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public class Fuente {

    @OneToMany
    @JoinColumn(name="url_fuente", referencedColumnName = "url")
    public List<Hecho> hechos = new ArrayList<>();

    @Id
    private String url;

    private String nombre;

    public Fuente(String url, String nombre) {
        this.url = url;
        this.nombre = nombre;
    }

    public Fuente() {

    }

    public void agregarHechos(List<Hecho> nuevosHechos) {
        this.hechos.addAll(nuevosHechos);
    }

    public void setHechos(List<Hecho> nuevosHechos) {
        this.hechos = nuevosHechos;
    }

    public UriComponentsBuilder armarParametrosConsulta(LocalDateTime ultimaConsulta) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        if (ultimaConsulta != null) {
            String ultimaConsultaString = ultimaConsulta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            builder.queryParam("ultimaConsulta", ultimaConsultaString);
        }

        return builder;

    }

    public void realizarConsulta(URI uri, WebClient webClient, Conversor conversor) {
        OrigenFuente origenFuente;
        List<HechoDTO> hechos = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList()
                .block();

        if (hechos != null) {
            origenFuente = OrigenFuente.getOrigenFuente(this.nombre);

            this.agregarHechos(hechos.stream().map(h -> conversor.convertirHecho(h, origenFuente)).toList());
        }

    }

}
