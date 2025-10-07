package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.agregador.models.entities.conversor.Conversor;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.Estatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ArchivoProcesadoRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ContribuyenteRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.OrigenFuenteRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.persistence.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

@Entity
@DiscriminatorValue("estatica")
public class FuenteEstatica extends Fuente{

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="url_fuente", referencedColumnName = "url")
    private List<ArchivoProcesado> archivosProcesados;

    public FuenteEstatica() {}

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
    @Transactional
    public void realizarConsulta(URI uri, WebClient webClient, Conversor conversor, ContribuyenteRepository contribuyenteRepository, ArchivoProcesadoRepository archivoProcesadoRepository, OrigenFuenteRepository origenFuenteRepository) {
        List<ArchivoProcesadoDTO> archivosDTO = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(ArchivoProcesadoDTO.class)
                .collectList()
                .block();

        if (archivosDTO != null) {
            for (ArchivoProcesadoDTO archivoDTO : archivosDTO) {
                // Crear el archivo procesado
                ArchivoProcesado archivoProcesado = new ArchivoProcesado(archivoDTO.getNombre(), archivoDTO.getFechaCarga());

                ArchivoProcesado archPersistido = archivoProcesadoRepository.saveAndFlush(archivoProcesado);

                // Usar el constructor con parámetro en lugar de setArchivoProcesado
                Estatica estatica = new Estatica(archPersistido);
                estatica = (Estatica) origenFuenteRepository.saveAndFlush(estatica);

                // Procesar los hechos con este origen
                List<Hecho> hechos = new ArrayList<>();
                for (HechoDTO hechoDTO : archivoDTO.getHechos()) {
                    Hecho hecho = conversor.convertirHecho(hechoDTO, estatica, contribuyenteRepository);
                    hechos.add(hecho);
                }
                // Agregar a la colección
                if (this.archivosProcesados == null) {
                    this.archivosProcesados = new ArrayList<>();
                }
                this.archivosProcesados.add(archPersistido);

                this.agregarHechos(hechos);
            }
        }
    }
}
