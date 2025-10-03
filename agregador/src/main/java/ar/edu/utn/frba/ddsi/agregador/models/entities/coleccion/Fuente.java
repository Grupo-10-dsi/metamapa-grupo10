package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "fuente", fetch = FetchType.EAGER)
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
        for (Hecho hecho : nuevosHechos) {
            hecho.setFuente(this);
            this.hechos.add(hecho);
        }
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
