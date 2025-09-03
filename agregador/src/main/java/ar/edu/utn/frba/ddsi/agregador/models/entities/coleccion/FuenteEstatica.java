package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.Estatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
public class FuenteEstatica extends Fuente{

    private List<ArchivoProcesado> archivosProcesados;

    public FuenteEstatica(String nombre, String url, List<ArchivoProcesado> archivosProcesados) {
        super(url, nombre);
        this.archivosProcesados = archivosProcesados;
    }

    @Override
    public UriComponentsBuilder armarParametrosConsulta(LocalDateTime ultimaConsulta) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.getUrl());
        List<String> archivosDTO = new ArrayList<>();

        for(ArchivoProcesado archivo : archivosProcesados){
             archivosDTO.add(archivo.getNombre());
        }

        builder.queryParam("archivosProcesados", String.join("&archivosProcesados=", archivosDTO));

        return builder;
    }

    @Override
    public void realizarConsulta(URI uri, WebClient webClient, Conversor conversor) {
        List<ArchivoProcesadoDTO> archivosDTO = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(ArchivoProcesadoDTO.class)
                .collectList()
                .block();


        if (archivosDTO != null) {
            for (ArchivoProcesadoDTO archivoDTO : archivosDTO) {
                ArchivoProcesado archivoProcesado = new ArchivoProcesado(archivoDTO.getNombre(), archivoDTO.getFechaCarga());
                OrigenFuente origenFuente = new Estatica(archivoProcesado);
                this.archivosProcesados.add(archivoProcesado);
                this.agregarHechos(archivoDTO.getHechos().stream().map(h -> conversor.convertirHecho(h, origenFuente)).toList());
            }
        }

    }
}
